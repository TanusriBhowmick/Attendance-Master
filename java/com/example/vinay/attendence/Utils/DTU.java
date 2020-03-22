package com.example.vinay.attendence.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class DTU {

    public final static String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public final static String HM = "HH:mm";
    public static String time;
    public static int currentHour, currentMinute, currentSeconds;
    public static int currentYear, currentMonth, currentDay;
    public static final int FLAG_ONLY_NEW = 1;
    public static final int FLAG_OLD_AND_NEW = 2;
    public static final int FLAG_OLD_ONLY = 3;


    public static String showTime24HourPickerDialogWithMinute(final Context appContext, String inputTime,
                                                              final EditText eStartTime) {

        final Calendar c = Calendar.getInstance();
        String[] spiltedTime = inputTime.split(":");

        currentHour = Integer.parseInt(spiltedTime[0]);
        currentMinute = Integer.parseInt(spiltedTime[1]);
        currentSeconds = 00;
        TimePickerDialog tpd = new TimePickerDialog(appContext,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minutes) {
                        int hour = hourOfDay;
                        int minute = roundTo5(minutes);
                        if (minute == 60) {
                            minute = 0;
                            hour = hour + 1;
                        }
                        String time = "" + hourOfDay + "" + minutes + "00";
                        if (minute < 10) {
                            int times = toMins(hour + ":" + minute);
                            eStartTime.setText("" + times);
                        } else {
                            int times = toMins(hour + ":" + minute);
                            eStartTime.setText("" + times);
                        }

                    }
                }, currentHour, currentMinute, true);
        tpd.show();

        return "";
    }


    public static String showTime24HourPickerDialog(final Context appContext, String inputTime,
                                                    final EditText eStartTime) {

        final Calendar c = Calendar.getInstance();
        String[] spiltedTime = inputTime.split(":");

        currentHour = Integer.parseInt(spiltedTime[0]);
        currentMinute = Integer.parseInt(spiltedTime[1]);
        currentSeconds = 00;
        TimePickerDialog tpd = new TimePickerDialog(appContext,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minutes) {
                        int hour = hourOfDay;
                        int minute = roundTo5(minutes);
                        if (minute == 60) {
                            minute = 0;
                            hour = hour + 1;
                        }
                        String time = "" + hourOfDay + "" + minutes + "00";

                        if (hour < 10 && minute < 10) {
                            eStartTime.setText("0" + hour + ":0" + minute);
                        } else if (hour < 10) {
                            eStartTime.setText("0" + hour + ":" + minute);
                        } else if (minute < 10) {
                            eStartTime.setText(hour + ":0" + minute);
                        } else {
                            eStartTime.setText(hour + ":" + minute);
                        }
                    }
                }, currentHour, currentMinute, true);
        tpd.show();

        return "";
    }


    private static int toMins(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

    public static String getCurrentDateTimeStamp(String format) {

        DateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        return s;
    }


    public static String getCurrentDate() {
        try {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int monthOfYear = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            String date_selected = "";

            if ((monthOfYear >= 0 && monthOfYear < 9)
                    && (dayOfMonth > 0 && dayOfMonth < 10))
                date_selected = String.valueOf(year) + "-0"
                        + String.valueOf(monthOfYear + 1) + "-0"
                        + String.valueOf(dayOfMonth);
            else if (monthOfYear >= 0 && monthOfYear < 9)
                date_selected = String.valueOf(year) + "-0"
                        + String.valueOf(monthOfYear + 1) + "-"
                        + String.valueOf(dayOfMonth);
            else if (dayOfMonth > 0 && dayOfMonth < 10)
                date_selected = String.valueOf(year) + "-"
                        + String.valueOf(monthOfYear + 1) + "-0"
                        + String.valueOf(dayOfMonth);
            else
                date_selected = String.valueOf(year) + "-"
                        + String.valueOf(monthOfYear + 1) + "-"
                        + String.valueOf(dayOfMonth);
            return date_selected;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getddmmyyDate(String dt) {
        // Converts mm-dd-yy format to dd-mm-yy Added on 05/12/2013
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


    public static String getyymmddDate(String dt)
    // Converts mm-dd-yy format to dd-mm-yy Added on 05/12/2013
    {
        String dd = "", mm = "", yy = "";
        int i = 0;
        try {
            for (String retval : dt.split("-")) {
                if (i == 0)
                    dd = retval;
                else if (i == 1)
                    mm = retval;
                else
                    yy = retval;
                i++;
            }
            return (yy + "-" + mm + "-" + dd).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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

    public static int roundTo5(double n) {
        return (int) Math.round(n / 5) * 5;
    }


    public static String showTimePickerDialogOnlyMinutes(final Context appContext, String inputTime,
                                                         final EditText eStartTime) {

        final Calendar c = Calendar.getInstance();
        String[] spiltedTime = inputTime.split(":");

        currentHour = Integer.parseInt(spiltedTime[0]);
        currentMinute = Integer.parseInt(spiltedTime[1]);
        currentSeconds = 00;
        TimePickerDialog tpd = new TimePickerDialog(appContext,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minutes) {
                        int minute = roundTo5(minutes);
                        if (minute == 60) {
                            minute = 0;
                        }
                        if (minute < 10) {
                            eStartTime.setText("0" + minute);
                            Log.e("IF_showTimePickerDialog", " " + eStartTime.getText().toString());
                        } else {
                            eStartTime.setText(""+minute);
                            Log.e("ELSE_showTimePicker", " " + eStartTime.getText().toString());
                        }
                    }
                }, currentHour, currentMinute, true);
        tpd.show();

        return "";
    }






}
