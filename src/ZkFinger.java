package src;

import com.zkteco.biometric.FingerprintSensorEx;
import src.entity.Employee;
import src.entity.Timecard;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ZkFinger {

    private static long devHandle = 0;
    private static long dbHandle = 0;

    public String init() {
        try {
            if (FingerprintSensorEx.Init() != 0) {
                return "SDK initialization failed.";
            }

            devHandle = FingerprintSensorEx.OpenDevice(0);
            if (devHandle == 0) {
                FingerprintSensorEx.Terminate();
                return "Failed to open fingerprint device.";
            }

            dbHandle = FingerprintSensorEx.DBInit();
            if (dbHandle == 0) {
                FingerprintSensorEx.CloseDevice(devHandle);
                FingerprintSensorEx.Terminate();
                return "Failed to initialize fingerprint DB.";
            }

            int width = getParam(devHandle, 1);
            int height = getParam(devHandle, 2);
            byte[] img = new byte[width * height];
            byte[] template = new byte[2048];
            int[] size = new int[]{2048};

            int result = -1;
            int attempts = 20;
            while (attempts-- > 0 && result != 0) {
                result = FingerprintSensorEx.AcquireFingerprint(devHandle, img, template, size);
                if (result == 0) break;
                Thread.sleep(500);
            }

            if (result != 0) {
                return "❌ Failed to acquire fingerprint. Error code: " + result;
            }

            int matchedId = matchAgainstDatabase(template);

            if (matchedId == 0) {
                return "Your fingerprint does not match to anyone.";
            }

            LocalDate localDate = LocalDate.now();
            Date sqlDate = Date.valueOf(localDate);
            Time sqlTime = Time.valueOf(LocalTime.now());
            Timecard timecard = new Timecard(matchedId, sqlDate, sqlTime, sqlTime);

            return TimeInAndOut(timecard);

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ An error occurred during verification.";
        } finally {
            cleanup();
        }
    }




    public static int matchAgainstDatabase(byte[] liveTemplate) {
        Connection conn = JDBC.getConnection();

        try {
            String sql = "SELECT employee_id, fingerprint FROM employees WHERE fingerprint IS NOT NULL";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int empId = rs.getInt("employee_id");
                byte[] dbTemplate = rs.getBytes("fingerprint");

                int score = FingerprintSensorEx.DBMatch(dbHandle, liveTemplate, dbTemplate);
                System.out.println("Matching with " + empId + " → Score: " + score);

                if (score > 0) { // You can set a threshold if needed
                    System.out.println("✅ MATCH FOUND: " + empId);
                    return empId;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static int getParam(long deviceHandle, int paramCode) {
        byte[] buf = new byte[4];
        int[] len = new int[]{4};
        FingerprintSensorEx.GetParameters(deviceHandle, paramCode, buf, len);
        return (buf[0] & 0xFF) | ((buf[1] & 0xFF) << 8);
    }

    private static void cleanup() {
        if (dbHandle != 0) {
            FingerprintSensorEx.DBFree(dbHandle);
            dbHandle = 0;
        }
        if (devHandle != 0) {
            FingerprintSensorEx.CloseDevice(devHandle);
            FingerprintSensorEx.Terminate();
            devHandle = 0;
        }
    }

    public String TimeInAndOut(Timecard timecard) {
        if (!hasTimecard(timecard)) {
            // No timecard yet → Insert time-in
            try {
                Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO timecards (employee_id, date, time_in) VALUES (?, ?, ?)");
                stmt.setInt(1, timecard.getEmployee_id());
                stmt.setDate(2, timecard.getDate());
                stmt.setTime(3, timecard.getTime_in());
                stmt.executeUpdate();
                return "✅ Time IN recorded!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "⚠️ Failed to record Time IN.";
            }
        } else if (isTimeOutNull(timecard)) {
            // Time-in exists, but no time-out → update time-out
            try {
                Connection conn = JDBC.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE timecards SET time_out = ? WHERE employee_id = ? AND date = ?");
                stmt.setTime(1, timecard.getTime_out());
                stmt.setInt(2, timecard.getEmployee_id());
                stmt.setDate(3, timecard.getDate());
                stmt.executeUpdate();
                return "✅ Time OUT recorded!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "⚠️ Failed to record Time OUT.";
            }
        } else {
            // Both time-in and time-out already exist
            return "🟡 Timecard already completed for today.";
        }
    }


    public boolean hasTimecard(Timecard timecard) {
        String sql = "SELECT 1 FROM timecards WHERE employee_id = ? AND date = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timecard.getEmployee_id());
            stmt.setDate(2, timecard.getDate());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isTimeOutNull(Timecard timecard) {
        String sql = "SELECT 1 FROM timecards WHERE employee_id = ? AND date = ? AND time_out IS NULL";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timecard.getEmployee_id());
            stmt.setDate(2, timecard.getDate());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}