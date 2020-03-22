package com.example.vinay.attendence.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.example.vinay.attendence.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.volley.VolleyLog.TAG;

public class VU {
    public final static String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public final static String YMD = "yyyy-MM-dd";
    public static final String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+";
    public static Pattern pattern;
    static Matcher matcher;
    public static final String MOBILE_PATTERN = "[0-9]{10}";

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public final static String HM = "HH:mm";
    public static String time;
    public static int currentHour, currentMinute, currentSeconds;
    public static int currentYear, currentMonth, currentDay;
    public static final int FLAG_ONLY_NEW = 1;
    public static final int FLAG_OLD_AND_NEW = 2;
    public static final int FLAG_OLD_ONLY = 3;


    public static boolean isEmpty(EditText editText) {
        // TODO method to check edit text is fill or no
        // return true when edit text is empty
        if (editText.getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isEmailId(EditText editText) {
        // method to check edit text is fill or no
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(editText.getText().toString().trim());
        if (matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isConnectingToInternet(Context appContext) {
        // Method to check internet connection
        ConnectivityManager conMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            // Toast.makeText(appContext, "No internet connection.", Toast.LENGTH_SHORT).show();
            showCustomDialog(appContext);
            return false;
        }


    }

    public static void showCustomDialog(final Context appContext) {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent((Settings.ACTION_SETTINGS));
                PackageManager packageManager = appContext.getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    appContext.startActivity(intent);
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public static String getddmmyyDate(String dt) {
        String dd = "", mm = "", yy = "";
        int i = 0;
        try {
            for (String retval : dt.split("-")) {
                if (i == 0)
                    yy = retval;
                else if (i == 1)
                    mm = retval;
                else
                    dd = retval;
                i++;
            }
            return (dd + "-" + mm + "-" + yy).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDateTimeStamp(String format) {

        DateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }

    public static void showDatePickerDialog(Context context, final int dateFlg,
                                            final EditText dateEditText) {
        // Displays Date picker
        final Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datepicker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int monthOfYear, int dayOfMonth) {
                        int year = selectedYear;
                        int month = monthOfYear;
                        int day = dayOfMonth;
                        if (dateFlg == FLAG_ONLY_NEW) {
                            if ((year != currentYear)
                                    || (month < currentMonth && year == currentYear)
                                    || (day < currentDay && year == currentYear && month <= currentMonth)) {
                                // showToastShort(appContext, "ECS",
                                // "Please select proper date.");
                                dateEditText
                                        .setText(getCurrentDateTimeStamp(""));

                            } else {
                                String date_selected;
                                if ((monthOfYear >= 0 && monthOfYear < 9)
                                        && (dayOfMonth > 0 && dayOfMonth < 10))
                                    date_selected = "0"
                                            + String.valueOf(dayOfMonth) + "-0"
                                            + String.valueOf(monthOfYear + 1)
                                            + "-"
                                            + String.valueOf(selectedYear);

                                else if (monthOfYear >= 0 && monthOfYear < 9)
                                    date_selected = String.valueOf(dayOfMonth)
                                            + "-0"
                                            + String.valueOf(monthOfYear + 1)
                                            + "-"
                                            + String.valueOf(selectedYear);

                                else if (dayOfMonth > 0 && dayOfMonth < 10)
                                    date_selected = "0"
                                            + String.valueOf(dayOfMonth) + "-"
                                            + String.valueOf(monthOfYear + 1)
                                            + "-"
                                            + String.valueOf(selectedYear);

                                else
                                    date_selected = String.valueOf(dayOfMonth)
                                            + "-"
                                            + String.valueOf(monthOfYear + 1)
                                            + "-"
                                            + String.valueOf(selectedYear);

                                dateEditText.setText(date_selected);
                            }
                        } else if (dateFlg == FLAG_OLD_AND_NEW) {
                            String date_selected;
                            if ((monthOfYear >= 0 && monthOfYear < 9)
                                    && (dayOfMonth > 0 && dayOfMonth < 10))
                                date_selected = "0"
                                        + String.valueOf(dayOfMonth) + "-0"
                                        + String.valueOf(monthOfYear + 1) + "-"
                                        + String.valueOf(selectedYear);
                            else if (monthOfYear >= 0 && monthOfYear < 9)
                                date_selected = String.valueOf(dayOfMonth)
                                        + "-0"
                                        + String.valueOf(monthOfYear + 1) + "-"
                                        + String.valueOf(selectedYear);
                            else if (dayOfMonth > 0 && dayOfMonth < 10)
                                date_selected = "0"
                                        + String.valueOf(dayOfMonth) + "-"
                                        + String.valueOf(monthOfYear + 1) + "-"
                                        + String.valueOf(selectedYear);
                            else
                                date_selected = String.valueOf(dayOfMonth)
                                        + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" + String.valueOf(selectedYear);
                            dateEditText.setText(date_selected);
                        } else if (dateFlg == FLAG_OLD_ONLY) {
                            String date_selected;
                            if ((monthOfYear >= 0 && monthOfYear < 9)
                                    && (dayOfMonth > 0 && dayOfMonth < 10))
                                date_selected = "0"
                                        + String.valueOf(dayOfMonth) + "-0"
                                        + String.valueOf(monthOfYear + 1) + "-"
                                        + String.valueOf(selectedYear);
                            else if (monthOfYear >= 0 && monthOfYear < 9)
                                date_selected = String.valueOf(dayOfMonth) + "-0"
                                        + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(selectedYear);
                            else if (dayOfMonth > 0 && dayOfMonth < 10)
                                date_selected = "0" + String.valueOf(dayOfMonth)
                                        + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(selectedYear);
                            else
                                date_selected = String.valueOf(dayOfMonth)
                                        + "-" + String.valueOf(monthOfYear + 1)
                                        + "-" + String.valueOf(selectedYear);
                            dateEditText.setText(date_selected);

                        }
                    }
                }, currentYear, currentMonth, currentDay);
        if (dateFlg == FLAG_OLD_ONLY) {
            datepicker.getDatePicker().setMaxDate(new Date().getTime());
        }
        datepicker.show();
    }
}
