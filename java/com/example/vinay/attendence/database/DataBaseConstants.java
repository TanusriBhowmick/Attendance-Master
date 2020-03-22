package com.example.vinay.attendence.database;

public class DataBaseConstants {

    public static final String DATABASE_NAME = "Attendance.db";
    public static final int DATABASE_VERSION = 1;

    public static class TableNames {
        public static final String TBL_PROFILE = "tbl_Profile";
        public static final String TBL_STUDENT_REG = "tbl_student_reg";
    }
    public static class ProfileData {

        public static final String NAME = "name";
        public static final String EMPLOYEE_ID = "employee_id";
        public static final String DEPARTMENT = "department";
        public static final String MOBILE_NO = "mobile_no";
        public static final String EMAIL_ID = "email_id";
        public static final String PASSWORD = "password";
        public static final String EMP_TYPE = "emp_type";
    }

    public static class StudentRegistrationData {

        public static final String NAME = "name";
        public static final String ENROLLMENT_NO = "enrollment_no";
        public static final String DEPARTMENT = "department";
        public static final String SEMESTER = "semester";
        public static final String EMAIL_ID = "email_id";
        public static final String MOBILE_NO = "mobile_no";
        public static final String FINGER1A = "finger_1a";
        public static final String FINGER1B = "finger_1b";
        public static final String FINGER1C = "finger_1c";
        public static final String FINGER2A = "finger_2a";
        public static final String FINGER2B = "finger_2b";
        public static final String FINGER2C = "finger_2c";
    }

}