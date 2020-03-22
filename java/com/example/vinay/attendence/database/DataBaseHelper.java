package com.example.vinay.attendence.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.vinay.attendence.ModelClasses.ModelProfileData;
import com.example.vinay.attendence.ModelClasses.ModelStudentRegistrationData;

import org.json.JSONArray;
import org.json.JSONObject;


public class DataBaseHelper {
    public static SQLiteDatabase db;
    public Context context;
    private SQLiteHelper sqliteopenhelper;
    private JSONArray jArray;
    private boolean isExist = false;
    private String str_column_name;
    private String TAG = "DataBaseHelper";
    private JSONObject json_data;

    public DataBaseHelper(Context context) {
        this.context = context;
        sqliteopenhelper = new SQLiteHelper(context);
        db = sqliteopenhelper.getWritableDatabase();
        db = sqliteopenhelper.getReadableDatabase();
    }

    public static  class DBProfileData {
        String TAG = "DBProfileData ";
        public static long profile_insert_id =0;

        //TODO Primary methods
        public static boolean insert(ModelProfileData object) {

            // db.execSQL("delete from "+ DataBaseConstants.TableNames.TBL_PRACTICE_EXAM);
            ContentValues cv = new ContentValues();
            cv.put(DataBaseConstants.ProfileData.NAME, object.getName());
            cv.put(DataBaseConstants.ProfileData.EMPLOYEE_ID, object.getEmployee_id());
            cv.put(DataBaseConstants.ProfileData.DEPARTMENT, object.getDepartment());
            cv.put(DataBaseConstants.ProfileData.MOBILE_NO, object.getMobile_no());
            cv.put(DataBaseConstants.ProfileData.EMAIL_ID, object.getEmail_id());
            cv.put(DataBaseConstants.ProfileData.PASSWORD, object.getPassword());
            cv.put(DataBaseConstants.ProfileData.EMP_TYPE, object.getEmp_type());
            Log.e("ContentValues",":"+cv.toString());

            try {
                profile_insert_id = db.insert(DataBaseConstants.TableNames.TBL_PROFILE, null, cv);
                Log.e("insertdata", ": " + profile_insert_id);
            } catch (Exception e) {
            }
            return true;

        }

        public static boolean updateProfile(String name,String employee_id,String mb_no,String email_id,
                                        String department,String emp_type) {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseConstants.ProfileData.NAME, name);
            cv.put(DataBaseConstants.ProfileData.EMPLOYEE_ID, employee_id);
            cv.put(DataBaseConstants.ProfileData.MOBILE_NO, mb_no);
            cv.put(DataBaseConstants.ProfileData.EMAIL_ID,email_id);
            cv.put(DataBaseConstants.ProfileData.DEPARTMENT, department);
            cv.put(DataBaseConstants.ProfileData.EMP_TYPE, emp_type);

            Log.e("DBUPdate",name+employee_id+mb_no+email_id+department);
            Log.e("CV",cv.toString());


            db.update(DataBaseConstants.TableNames.TBL_PROFILE, cv, DataBaseConstants.ProfileData.EMPLOYEE_ID +
                                        "= ?",new String[] { employee_id});
            return true;
        }

        public static Cursor getProfileData(String EmployeeId) {
            Cursor res = db.rawQuery("select * from "+DataBaseConstants.TableNames.TBL_PROFILE +" where "+ DataBaseConstants.ProfileData.EMPLOYEE_ID+" = '"+EmployeeId+"'",null);
            return res;
        }



        //TODO: method to get list of profileData
        public static JSONArray getProfileList(String EmployeeId) {
            JSONArray array = new JSONArray();
            try {
                Cursor cursor = null;
                Log.e("db_EmployeeId",":"+EmployeeId);
                Log.e("CURSOR_QUERY"," : "+"select * from " + DataBaseConstants.TableNames.TBL_PROFILE + " where  " + DataBaseConstants.ProfileData.EMPLOYEE_ID +  "=" + EmployeeId );
                cursor = db.rawQuery("select * from " + DataBaseConstants.TableNames.TBL_PROFILE + " where  " + DataBaseConstants.ProfileData.EMPLOYEE_ID + "=" + EmployeeId , null);


                while (cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(DataBaseConstants.ProfileData.NAME, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.NAME)));
                    jsonObject.put(DataBaseConstants.ProfileData.EMPLOYEE_ID, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.EMPLOYEE_ID)));
                    jsonObject.put(DataBaseConstants.ProfileData.DEPARTMENT, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.DEPARTMENT)));
                    jsonObject.put(DataBaseConstants.ProfileData.MOBILE_NO, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.MOBILE_NO)));
                    jsonObject.put(DataBaseConstants.ProfileData.EMAIL_ID, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.EMAIL_ID)));
                    jsonObject.put(DataBaseConstants.ProfileData.PASSWORD, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.PASSWORD)));
                    jsonObject.put(DataBaseConstants.ProfileData.EMP_TYPE, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.EMP_TYPE)));
                    array.put(jsonObject);

                    Log.e("getList_Method"," : "+array.toString());
                }
                return array;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static  class DBStudentRegData {
        String TAG = "DBStudentRegData ";
        public static long StudentReg_insert_id =0;

        //TODO Primary methods
        public static boolean insert(ModelStudentRegistrationData object) {

            // db.execSQL("delete from "+ DataBaseConstants.TableNames.TBL_PRACTICE_EXAM);
            ContentValues cv = new ContentValues();
            cv.put(DataBaseConstants.StudentRegistrationData.NAME, object.getName());
            cv.put(DataBaseConstants.StudentRegistrationData.ENROLLMENT_NO, object.getEnrollmentNo());
            cv.put(DataBaseConstants.StudentRegistrationData.DEPARTMENT, object.getDepartment());
            cv.put(DataBaseConstants.StudentRegistrationData.MOBILE_NO, object.getMobileNo());
            cv.put(DataBaseConstants.StudentRegistrationData.EMAIL_ID, object.getEmail());
            cv.put(DataBaseConstants.StudentRegistrationData.SEMESTER, object.getSemester());
            cv.put(DataBaseConstants.StudentRegistrationData.FINGER1A, object.getFinger1A());
           /*    cv.put(DataBaseConstants.StudentRegistrationData.FINGER1B, object.getFinger1B());
            cv.put(DataBaseConstants.StudentRegistrationData.FINGER1C, object.getFinger1C());
           // cv.put(DataBaseConstants.StudentRegistrationData.FINGER2A, object.getFinger2A());
            cv.put(DataBaseConstants.StudentRegistrationData.FINGER2B, object.getFinger2B());
            cv.put(DataBaseConstants.StudentRegistrationData.FINGER2C, object.getFinger2C());*/

            Log.e("ContentValues",":"+cv.toString());
            try {
                StudentReg_insert_id = db.insert(DataBaseConstants.TableNames.TBL_STUDENT_REG, null, cv);
                Log.e("", ": " + StudentReg_insert_id);
            } catch (Exception e) {
            }
            return true;

        }

        public static boolean updateProfile(String name,String employee_id,String mb_no,String email_id,
                                            String department,String emp_type) {
            ContentValues cv = new ContentValues();
            cv.put(DataBaseConstants.ProfileData.NAME, name);
            cv.put(DataBaseConstants.ProfileData.EMPLOYEE_ID, employee_id);
            cv.put(DataBaseConstants.ProfileData.MOBILE_NO, mb_no);
            cv.put(DataBaseConstants.ProfileData.EMAIL_ID,email_id);
            cv.put(DataBaseConstants.ProfileData.DEPARTMENT, department);
            cv.put(DataBaseConstants.ProfileData.EMP_TYPE, emp_type);

            Log.e("DBUPdate",name+employee_id+mb_no+email_id+department);
            Log.e("CV",cv.toString());


            db.update(DataBaseConstants.TableNames.TBL_PROFILE, cv, DataBaseConstants.ProfileData.EMPLOYEE_ID +
                    "= ?",new String[] { employee_id});
            return true;
        }

        public static Cursor getStudentRegData(String EnrollmentNo) {
            Cursor res = db.rawQuery("select * from "+DataBaseConstants.TableNames.TBL_STUDENT_REG +" where "+ DataBaseConstants.StudentRegistrationData.ENROLLMENT_NO+" = '"+EnrollmentNo+"'",null);
            return res;
        }



        //TODO: method to get list of profileData
        public static JSONArray getProfileList(String EmployeeId) {
            JSONArray array = new JSONArray();
            try {
                Cursor cursor = null;
                Log.e("db_EmployeeId",":"+EmployeeId);
                Log.e("CURSOR_QUERY"," : "+"select * from " + DataBaseConstants.TableNames.TBL_PROFILE + " where  " + DataBaseConstants.ProfileData.EMPLOYEE_ID +  "=" + EmployeeId );
                cursor = db.rawQuery("select * from " + DataBaseConstants.TableNames.TBL_PROFILE + " where  " + DataBaseConstants.ProfileData.EMPLOYEE_ID + "=" + EmployeeId , null);


                while (cursor.moveToNext()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(DataBaseConstants.ProfileData.NAME, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.NAME)));
                    jsonObject.put(DataBaseConstants.ProfileData.EMPLOYEE_ID, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.EMPLOYEE_ID)));
                    jsonObject.put(DataBaseConstants.ProfileData.DEPARTMENT, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.DEPARTMENT)));
                    jsonObject.put(DataBaseConstants.ProfileData.MOBILE_NO, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.MOBILE_NO)));
                    jsonObject.put(DataBaseConstants.ProfileData.EMAIL_ID, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.EMAIL_ID)));
                    jsonObject.put(DataBaseConstants.ProfileData.PASSWORD, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.PASSWORD)));
                    jsonObject.put(DataBaseConstants.ProfileData.EMP_TYPE, cursor.getString(cursor.getColumnIndex(DataBaseConstants.ProfileData.EMP_TYPE)));
                    array.put(jsonObject);

                    Log.e("getList_Method"," : "+array.toString());
                }
                return array;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}