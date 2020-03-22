package com.example.vinay.attendence.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ACU {

    public static class MySP {

        public static String MAIN_URL = new String("http://www.productplusphotography.in/app/");
//        public static String MAIN_URL = new String("http://prosolstechin.ipage.com/stapp/");

        public static String FRAGMENT_NAME = "fragmentName";
        public static String LOGIN_TYPE = "radio_login";
        public static String LOGIN_STATUS = "login_status";


        public static String PROFILE_NAME = "profile_name";
        public static String PROFILE_DEPARTMENT = "profile_department";
        public static String PROFILE_EMPLOYEE_ID = "profile_employee_id";
        public static String PROFILE_MOBILE_NO = "profile_mobile_no";
        public static String PROFILE_EMAIL_ID = "profile_email_id";

        public static String Take_ATTENDENCE_DATE = "take_attendence_date";
        public static String Take_ATTENDENCE_TYPE = "take_attendence_type";
        public static String Take_ATTENDENCE_EMPLOYEEID = "take_attendence_empId";
        public static String Take_ATTENDENCE_DEPARTMENT = "take_attendence_dpt";
        public static String Take_ATTENDENCE_COURSENAME = "take_attendence_courseName";
        public static String Take_ATTENDENCE_COURSEID = "take_attendence_courseId";
        public static String Take_ATTENDENCE_SEMESTER = "take_attendence_sem";
        public static String Take_ATTENDENCE_TIMEFROM = "take_attendence_timeFrom";
        public static String Take_ATTENDENCE_TIMETO = "take_attendence_timeTo";
        public static String Take_ATTENDENCE_BATCH = "take_attendence_batch";
        public static String Take_ATTENDENCE_SHIFT = "take_attendence_shift";

        public static String STUDENT_LIST = "student_list";
        public static String ENROLLMENT_NO_LIST = "enrollment_no_list";
        public static String FINGER_LIST = "finger_list";
        public static String FINGERS_LIST = "fingers_list";






        public static void saveSP(Context mContext, String key, String data) {
            final String PREFS_NAME = "SettingDetails";
            final SharedPreferences SpyAppData = mContext.getSharedPreferences(
                    PREFS_NAME, 0);
            SharedPreferences.Editor editor = SpyAppData.edit();
            editor.putString(key, data);
            editor.commit();
        }

        public static String getFromSP(Context mContext, String key, String dvalu) {
            final String PREFS_NAME = "SettingDetails";
            final SharedPreferences ToolsAppData = mContext.getSharedPreferences(
                    PREFS_NAME, 0);
            final String preData = ToolsAppData.getString(key, dvalu).trim();
            return preData;

        }


        public static String getSPString(Context context, String tag, String defltValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getString(tag, defltValue);
        }

        public static Boolean setSPString(Context context, String tag, String value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            try {
                sp.edit().putString(tag, value).apply();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public static Boolean  getSPBoolean(Context context, String tag, boolean defltValue) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            return sp.getBoolean(tag, defltValue);
        }

        public static Boolean setSPBoolean(Context context, String tag, boolean value) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            try {
                sp.edit().putBoolean(tag, value).apply();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public static  void saveArrayList(Context context,String key,ArrayList<String> list){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            editor.putString(key, json);
            editor.apply();     // This line is IMPORTANT !!!
        }

        public static ArrayList<String> getArrayList(Context context,String key){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = prefs.getString(key, null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            return gson.fromJson(json, type);
        }

        public static  void saveArrayByteList(Context context,String key,ArrayList<byte[]> list){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            editor.putString(key, json);
            editor.apply();     // This line is IMPORTANT !!!
        }

        public static ArrayList<byte[]> getArrayByteList(Context context,String key){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = prefs.getString(key, null);
            Type type = new TypeToken<ArrayList<byte[]>>() {}.getType();
            return gson.fromJson(json, type);
        }

        public static  void saveArraysBytesList(Context context,String key,ArrayList<ArrayList<byte[]>> list){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            editor.putString(key, json);
            editor.apply();     // This line is IMPORTANT !!!
        }

        public static ArrayList<ArrayList<byte[]>> getArraysBytesList(Context context,String key){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = prefs.getString(key, null);
            Type type = new TypeToken<ArrayList<ArrayList<byte[]>>>() {}.getType();
            return gson.fromJson(json, type);
        }


    }
}
