package src;

import com.zkteco.biometric.FingerprintSensorEx;
import src.entity.Employee;
import src.entity.Timecard;

import java.sql.*;
import java.time.LocalDate;

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

            if (matchAgainstDatabase(template) == 0){
                return "Your fingerprint does not match to anyone.";
            }

            if (matchAgainstDatabase(template) != 0){
                LocalDate localDate = LocalDate.now();
                Date sqlDate = Date.valueOf(localDate);
                Timecard timecard = new Timecard(matchAgainstDatabase(template),sqlDate);
                return TimeInAndOut(timecard);
            }


            return "kaka";

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
        }
        if (devHandle != 0) {
            FingerprintSensorEx.CloseDevice(devHandle);
            FingerprintSensorEx.Terminate();
        }
    }

    public String TimeInAndOut(Timecard timecard){
        LocalDate today = LocalDate.now();
        String timeIn = "INSERT INTO timecards (employee_id, date, time_in) VALUES (?, ?, ?) FROM `payrollmsdb`.`timecards` ";
        String timeOut = "UPDATE payrollmsdb.timecards SET time_out = ? WHERE date = ? AND employee_id = ?";

        Connection conn = JDBC.getConnection();

        if (isTimecardExist(timecard)){
            return "Timecard already exist!";

        }

        if (isTimeInNull(timecard)){
            try {
                System.out.println("Wala pang time in go na");
                PreparedStatement stmt = conn.prepareStatement(timeIn);
                stmt.setInt(1, timecard.getEmployee_id());
                stmt.setDate(2, timecard.getDate());
                stmt.setTime(3,timecard.getTime_in());
                System.out.println("Recorded timein");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            System.out.println("Sa time out ilalagay");
            PreparedStatement stmt = conn.prepareStatement(timeOut);
            stmt.setTime(1,timecard.getTime_out());
            stmt.setDate(2,timecard.getDate());
            stmt.setInt(3,timecard.getEmployee_id());
            System.out.println("Recorded timeout");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "Done na";

    }

    public boolean isTimecardExist(Timecard timecard){
        String sql = "SELECT * FROM timecards WHERE employee_id = ? AND date = ?";
        Connection conn = JDBC.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timecard.getEmployee_id());
            stmt.setDate(2, timecard.getDate()); // example date

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
                // true if tapos na time card for that date
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public boolean isTimeInNull(Timecard timecard) {
        String checkTimeIn = "SELECT 1 FROM payrollmsdb.timecards WHERE date = ? AND time_in IS NULL";
        Connection conn = JDBC.getConnection();

        try {
            PreparedStatement stmt = conn.prepareStatement(checkTimeIn);
            stmt.setDate(1, timecard.getDate());

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returns true if any row exists (i.e., time_in is NULL for that date)
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}