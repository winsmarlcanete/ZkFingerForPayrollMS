# ZkFingerForPayrollMS


This Java application simulates a biometric scanner using the **ZKFinger SDK** to capture employee **time-in and time-out** logs.  
It is designed to work alongside a payroll system by recording real-time attendance data and saving it to a local or remote database.

## Features

- Integrates with **ZKTeco** fingerprint scanners via **ZKFinger SDK**
- Captures real-time **time-in** and **time-out** events
- Matches fingerprint data with registered employee IDs
- Logs attendance to a MySQL database or exports to a CSV
- Displays status messages for scan success, failure, or invalid ID

## Technologies Used

- Java 
- ZKFinger SDK (ZKTeco)
- MySQL 


## Requirements

- ZKTeco fingerprint scanner device
- ZKFinger SDK and its required native libraries (DLL files)
- Java Development Kit (JDK)
- JDBC connector (for database use)

## Notes

- This application is a standalone attendance logger that can is only used by other repository called PayrollMS.


## Disclaimer

This project is for academic and integration purposes only. ZKTeco and ZKFinger SDK are trademarks of their respective owners.
