package com.example.vinay.attendence.database;


public class SQLiteQueries {



    public static final String CREATE_profile_TABLE = "create table IF NOT EXISTS "
            + DataBaseConstants.TableNames.TBL_PROFILE + "( "
            + DataBaseConstants.ProfileData.EMP_TYPE + " VARCHAR,"
            + DataBaseConstants.ProfileData.NAME + " VARCHAR,"
            + DataBaseConstants.ProfileData.EMPLOYEE_ID +  " VARCHAR,"
            + DataBaseConstants.ProfileData.DEPARTMENT +  " VARCHAR,"
            + DataBaseConstants.ProfileData.MOBILE_NO + " VARCHAR,"
            + DataBaseConstants.ProfileData.EMAIL_ID + " VARCHAR ,"
            + DataBaseConstants.ProfileData.PASSWORD + " VARCHAR );";


    public static final String CREATE_STUDENT_REG_TABLE = "create table IF NOT EXISTS "
            + DataBaseConstants.TableNames.TBL_STUDENT_REG + "( "
            + DataBaseConstants.StudentRegistrationData.NAME + " VARCHAR,"
            + DataBaseConstants.StudentRegistrationData.ENROLLMENT_NO +  " VARCHAR,"
            + DataBaseConstants.StudentRegistrationData.DEPARTMENT +  " VARCHAR,"
            + DataBaseConstants.StudentRegistrationData.MOBILE_NO + " VARCHAR,"
            + DataBaseConstants.StudentRegistrationData.EMAIL_ID + " VARCHAR ,"
            + DataBaseConstants.StudentRegistrationData.SEMESTER + " VARCHAR ,"
            + DataBaseConstants.StudentRegistrationData.FINGER1A + " INTEGER  )";
           /* + DataBaseConstants.StudentRegistrationData.FINGER1A + " VARCHAR ),"
            + DataBaseConstants.StudentRegistrationData.FINGER1B + " VARCHAR ),"
            + DataBaseConstants.StudentRegistrationData.FINGER1C + " VARCHAR ),"
            + DataBaseConstants.StudentRegistrationData.FINGER2A + " VARCHAR ),"
            + DataBaseConstants.StudentRegistrationData.FINGER2B + " VARCHAR ),"
            + DataBaseConstants.StudentRegistrationData.FINGER2C + " VARCHAR )";*/

}
