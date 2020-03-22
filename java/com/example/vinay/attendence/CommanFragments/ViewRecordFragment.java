package com.example.vinay.attendence.CommanFragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ViewRecordFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    public ViewRecordFragment() {
        // Required empty public constructor
    }

    TableLayout table;
    AppCompatEditText studentTypeEt;
    AppCompatSpinner recordTypeSpn, semesterTypeSpn, sectionTypeSpn, subjectTypeSpn;
    View view;
    TextInputLayout recordLt, subLt, secLt, semLt, stdLt;
    ImageView shareImg, viewAllImg;
    String Fnamexls;
    Context context;
    ConnectionClass connectionClass = null;
    private static final int REQUEST_CODE = 1; //EXCEL FILE
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 12;
    String stringRecordType = null, stringSemesterType = null, stringSectionType = null,
            stringCourseType = null, stringEnrollmentNo = null;

    //ResultSet Strings

    String stringResultSetSession,stringResultSetEnrollmentNo,stringResultSetStud_Name,
            stringResultSetTPresent,stringResultSetTAbsent,stringResultSetPPresent,
            stringResultSetPAbsent;

    ArrayList<String> stringResultSetSessionList = null;
    ArrayList<String> stringResultSetEnrollmentNoList = null;
    ArrayList<String> stringResultSetStud_NameList = null;
    ArrayList<String> stringResultSetTPresentList = null;
    ArrayList<String> stringResultSetTAbsentList = null;
    ArrayList<String> stringResultSetPPresentList = null;
    ArrayList<String> stringResultSetPAbsentList = null;
    ArrayList<Double> stringPAvgList = null;
    ArrayList<Double> stringTotalAvgList = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_record, container, false);
        context = getActivity();
        connectionClass = new ConnectionClass();


        init();
        setSpinner();
        semSpinner();
        secSpinner();
        courseSpinner();
        askPermissions();

        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExcelFile(context);
                final Intent target = new Intent(Intent.ACTION_GET_CONTENT);
                // The MIME data type filter
                target.setType("*/*");
                // Only return URIs that can be opened with ContentResolver
                target.addCategory(Intent.CATEGORY_OPENABLE);
                Intent intent = Intent.createChooser(target, "excel");

                try {
                    startActivityForResult(intent, REQUEST_CODE);
                    //setTargetFragment(intent,REQUEST_CODE);
                } catch (Exception e) {

                }

            }
        });

        viewAllImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringResultSetSessionList = new ArrayList<>();
                stringResultSetEnrollmentNoList = new ArrayList<>();
                stringResultSetStud_NameList = new ArrayList<>();
                stringResultSetTPresentList = new ArrayList<>();
                stringResultSetTAbsentList = new ArrayList<>();
                stringResultSetPPresentList = new ArrayList<>();
                stringResultSetPAbsentList = new ArrayList<>();
                stringPAvgList = new ArrayList<>();
                stringTotalAvgList = new ArrayList<>();
                stringEnrollmentNo = studentTypeEt.getText().toString().trim();
                ViewRecordAsync viewRecordAsync = new ViewRecordAsync();
                viewRecordAsync.execute(stringEnrollmentNo, stringCourseType, stringSemesterType);
            }
        });

        return view;

    }

    private void init() {

        shareImg = view.findViewById(R.id.imgShare);
        viewAllImg = view.findViewById(R.id.imgViewAll);
        recordTypeSpn = view.findViewById(R.id.recordTypeSpn);
        semesterTypeSpn = view.findViewById(R.id.semesterTypeSpn);
        sectionTypeSpn = view.findViewById(R.id.sectionTypeSpn);
        subjectTypeSpn = view.findViewById(R.id.subjectTypeSpn);
        studentTypeEt = view.findViewById(R.id.studentTypeEt);
        table = (TableLayout) view.findViewById(R.id.mytable);


        recordLt = view.findViewById(R.id.recordLt);
        subLt = view.findViewById(R.id.subLt);
        secLt = view.findViewById(R.id.secLt);
        semLt = view.findViewById(R.id.semLt);
        stdLt = view.findViewById(R.id.stdLt);

    }

    private void secSpinner() {
        sectionTypeSpn.setSelection(0);
        sectionTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringSectionType = sectionTypeSpn.getSelectedItem().toString().trim();
                Log.e("stringSectionType", stringSectionType);
                switch (parent.getSelectedItemPosition()) {
                    case 1:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void semSpinner() {
        semesterTypeSpn.setSelection(0);
        semesterTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringSemesterType = semesterTypeSpn.getSelectedItem().toString().trim();
                Log.e("stringSemesterType", stringSemesterType);
                switch (parent.getSelectedItemPosition()) {
                    case 1:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void courseSpinner() {
        subjectTypeSpn.setSelection(0);
        subjectTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringCourseType = subjectTypeSpn.getSelectedItem().toString().trim();  //course type
                Log.e("stringCourseType", stringCourseType);
                switch (parent.getSelectedItemPosition()) {
                    case 1:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinner() {
        recordTypeSpn.setSelection(0);
        recordTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringRecordType = recordTypeSpn.getSelectedItem().toString().trim();
                Log.e("stringRecordType", stringRecordType);
                switch (parent.getSelectedItemPosition()) {
                    case 1:
                        subjectTypeSpn.setVisibility(View.VISIBLE);
                        semesterTypeSpn.setVisibility(View.VISIBLE);
                        subLt.setVisibility(View.VISIBLE);
                        semLt.setVisibility(View.VISIBLE);

                        sectionTypeSpn.setVisibility(View.GONE);
                        secLt.setVisibility(View.GONE);
                        studentTypeEt.setVisibility(View.GONE);
                        stdLt.setVisibility(View.GONE);

                        subjectRecord();
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        semesterTypeSpn.setVisibility(View.VISIBLE);
                        semLt.setVisibility(View.VISIBLE);

                        subjectTypeSpn.setVisibility(View.GONE);
                        subLt.setVisibility(View.GONE);
                        sectionTypeSpn.setVisibility(View.GONE);
                        secLt.setVisibility(View.GONE);
                        studentTypeEt.setVisibility(View.GONE);
                        stdLt.setVisibility(View.GONE);

                        semesterRecord();
                        Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        studentTypeEt.setVisibility(View.VISIBLE);
                        stdLt.setVisibility(View.VISIBLE);
                        semesterTypeSpn.setVisibility(View.VISIBLE);
                        semLt.setVisibility(View.VISIBLE);

                        subjectTypeSpn.setVisibility(View.GONE);
                        subLt.setVisibility(View.GONE);
                        sectionTypeSpn.setVisibility(View.GONE);
                        secLt.setVisibility(View.GONE);


                        studentRecord();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void subjectRecord() {
    }

    private void semesterRecord() {
    }

    private void studentRecord() {
    }


    public void tableInit() {
        table.removeAllViews();
        table.setVisibility(view.VISIBLE);
        TableRow tbrow0 = new TableRow(getActivity());
        tbrow0.setGravity(Gravity.CENTER);

//        tbrow0.setBackgroundResource(R.drawable.row_border);

//        TableRow.LayoutParams params = (TableRow.LayoutParams) tbrow0.getLayoutParams();
//        params.height = 50;
//        tbrow0.setLayoutParams(params);

        TextView tv0 = new TextView(getActivity());
        tv0.setText(" Enroll.\n  No. ");
        tv0.setTextColor(Color.WHITE);
        tv0.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(getActivity());
        tv1.setText(" Student\n  Name ");
        tv1.setTextColor(Color.WHITE);
        tv1.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setText("  Total\nSession ");
        tv2.setTextColor(Color.WHITE);
        tv2.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(getActivity());
        tv3.setText(" \n  Present ");
        tv3.setTextColor(Color.WHITE);
        tv3.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(getActivity());
        tv4.setText("  Theory\n  Absent ");
        tv4.setTextColor(Color.WHITE);
        tv4.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv4);

        TextView tv5 = new TextView(getActivity());
        tv5.setText(" \n Average ");
        tv5.setTextColor(Color.WHITE);
        tv5.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv5);

        TextView tv6 = new TextView(getActivity());
        tv6.setText(" \n  Present ");
        tv6.setTextColor(Color.WHITE);
        tv6.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv6);

        TextView tv7 = new TextView(getActivity());
        tv7.setText(" Practical\n  Absent ");
        tv7.setTextColor(Color.WHITE);
        tv7.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv7);

        TextView tv8 = new TextView(getActivity());
        tv8.setText("\nAverage ");
        tv8.setTextColor(Color.WHITE);
        tv8.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv8);

        TextView tv9 = new TextView(getActivity());
        tv9.setText("   \n Mean(%) ");
        tv9.setTextColor(Color.WHITE);
        tv9.setBackgroundResource(R.drawable.row_border);
        tbrow0.addView(tv9);

        table.addView(tbrow0);

        for (int i = 0; i < stringResultSetEnrollmentNoList.size(); i++) {
            TableRow tbrow = new TableRow(getActivity());

            TextView t0v = new TextView(getActivity());
            t0v.setText(stringResultSetEnrollmentNoList.get(i));
            t0v.setTextColor(Color.BLACK);
            t0v.setGravity(Gravity.CENTER);
            tbrow.addView(t0v);


            TextView t1v = new TextView(getActivity());
            t1v.setText(stringResultSetStud_NameList.get(i));
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            TextView t2v = new TextView(getActivity());
            t2v.setText(stringResultSetSessionList.get(i));
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);

            TextView t3v = new TextView(getActivity());
            t3v.setText(stringResultSetTPresentList.get(i));
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            TextView t4v = new TextView(getActivity());
            t4v.setText(stringResultSetTAbsentList.get(i));
            t4v.setTextColor(Color.BLACK);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);

            TextView t5v = new TextView(getActivity());
            double totalSess= Double.parseDouble(stringResultSetSessionList.get(i));
            double tPreAvg= Double.parseDouble(stringResultSetTPresentList.get(i));
            double tAbsAvg= Double.parseDouble(stringResultSetTAbsentList.get(i));
            double theoryAvg= (tPreAvg)*100/(tPreAvg+tAbsAvg);

            t5v.setText(""+theoryAvg+"%");
            t5v.setTextColor(Color.BLACK);
            t5v.setGravity(Gravity.CENTER);
            tbrow.addView(t5v);

            TextView t6v = new TextView(getActivity());
            t6v.setText(stringResultSetPPresentList.get(i));
            t6v.setTextColor(Color.BLACK);
            t6v.setGravity(Gravity.CENTER);
            tbrow.addView(t6v);

            TextView t7v = new TextView(getActivity());
            t7v.setText(stringResultSetPAbsentList.get(i));
            t7v.setTextColor(Color.BLACK);
            t7v.setGravity(Gravity.CENTER);
            tbrow.addView(t7v);

            TextView t8v = new TextView(getActivity());
            double pPreAvg= Double.parseDouble(stringResultSetPPresentList.get(i));
            double pAbsAvg= Double.parseDouble(stringResultSetPAbsentList.get(i));

            double practAvg = (pPreAvg) * 100 / (pPreAvg + pAbsAvg);
            if (Double.isNaN(practAvg)) {
                practAvg = 0.0;
            }
            stringPAvgList.add(practAvg);
            t8v.setText("" + practAvg + "%");
            t8v.setTextColor(Color.BLACK);
            t8v.setGravity(Gravity.CENTER);
            tbrow.addView(t8v);

            TextView t9v = new TextView(getActivity());
            double totalAvg= (tPreAvg+pPreAvg)*100/totalSess;
            if (Double.isNaN(totalAvg)) {
                totalAvg = 0.0;
            }
            stringTotalAvgList.add(totalAvg);
            t9v.setText(""+totalAvg+"%");
            t9v.setTextColor(Color.BLACK);
            t9v.setGravity(Gravity.CENTER);
            tbrow.addView(t9v);

            table.addView(tbrow);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final Uri uri = data.getData();
        //  final  Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Attendance"+Fnamexls);
        Log.e("URI", uri.toString());
        try {
            final String path = FileUtils.getPath(context, uri);
            Toast.makeText(context, "file selected: " + path, Toast.LENGTH_SHORT).show();
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("*/*");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));

        } catch (Exception e) {
        }

        //   super.onActivityResult(requestCode, resultCode, data);

    }

    private void saveExcelFile(Context context) {
        // for directry
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("storage", "Storage not available..FreeUp some space");
        }
        String pattern = "yyyyMMddHH_mm ";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, new Locale("da", "DK"));

        String date = simpleDateFormat.format(new Date());
        System.out.println(date);

        Fnamexls = date + "Excel.xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Attendance");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        Log.e("FILE", file.toString());

        //Excel code
        WorkbookSettings wbSettings;
        wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        try {
            int a = 1;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //workbook.createSheet("Report", 0);
            WritableSheet sheet = workbook.createSheet("Attendence Sheet", 0);

            Label label0 = new Label(0, 0, "Enrollment No.");
            Label label3 = new Label(1, 0, "Student Name ");
            Label label4 = new Label(2, 0, "Total Session");
            Label label5 = new Label(3, 0, "Present(T)");
            Label label6 = new Label(4, 0, "Absent(T)");
            Label label7 = new Label(5, 0, "Average(T)");
            Label label8 = new Label(6, 0, "Present(P)");
            Label label9 = new Label(7, 0, "Absent(P)");
            Label label10 = new Label(8, 0, "Average(P)");
            Label label11 = new Label(9, 0, "Total Avg(%)");

            try {
                for (int i = 1; i < (stringResultSetEnrollmentNoList.size()+1); i++) {
                    double totalAvg = stringTotalAvgList.get(i-1);
                    double PAvg = stringPAvgList.get(i-1);
                    Label enrollData = new Label(0, i, stringResultSetEnrollmentNoList.get(i-1));
                    Label StudentData = new Label(1, i, stringResultSetStud_NameList.get(i-1));
                    Label TotalSessionData = new Label(2, i, stringResultSetSessionList.get(i-1));
                    Label PresentTData = new Label(3, i, stringResultSetTPresentList.get(i-1));
                    Label AbsentTData = new Label(4, i, stringResultSetTAbsentList.get(i-1));
                    Label MeanTData = new Label(5, i, String.valueOf(totalAvg));
                    Label PresentPData = new Label(6, i, stringResultSetPPresentList.get(i-1));
                    Label AbsentPData = new Label(7, i, stringResultSetPAbsentList.get(i-1));
                    Label MeanPData = new Label(8, i, String.valueOf(totalAvg));
                    Label TotalAvgData = new Label(9, i,String.valueOf(totalAvg));
                    sheet.addCell(enrollData);
                    sheet.addCell(StudentData);
                    sheet.addCell(TotalSessionData);
                    sheet.addCell(PresentTData);
                    sheet.addCell(AbsentTData);
                    sheet.addCell(MeanTData);
                    sheet.addCell(PresentPData);
                    sheet.addCell(AbsentPData);
                    sheet.addCell(MeanPData);
                    sheet.addCell(TotalAvgData);

                }

                sheet.setColumnView(0, (16 * 450));
                sheet.setColumnView(1, (16 * 450));
                sheet.setColumnView(2, (14 * 550));
                sheet.setColumnView(3, (14 * 550));
                sheet.setColumnView(4, (14 * 550));
                sheet.setColumnView(5, (14 * 550));
                sheet.setColumnView(6, (14 * 550));
                sheet.setColumnView(7, (14 * 550));
                sheet.setColumnView(8, (14 * 550));
                sheet.setColumnView(9, (14 * 550));


                sheet.addCell(label0);
                sheet.addCell(label4);
                sheet.addCell(label3);
                sheet.addCell(label5);
                sheet.addCell(label6);
                sheet.addCell(label7);
                sheet.addCell(label8);
                sheet.addCell(label9);
                sheet.addCell(label10);
                sheet.addCell(label11);


            } catch (RowsExceededException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //createExcel(excelSheet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    private void askPermissions() {

        int permissionCheckStorage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // we already asked for permisson & Permission granted, call camera intent
        if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

            //do what you want

        } else {

            // if storage request is denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("You need to give permission to access storage in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);

            }

        }
    }

    public class ViewRecordAsync extends AsyncTask<String, String, ResultSet> {

        String stringEnrollmentNo = null;
        String stringcourse = null;
        String stringSem = null;
        String stringMonth = null;
        String stringSection = null;

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected ResultSet doInBackground(String... params) {

            stringEnrollmentNo = params[0];
            stringcourse = params[1];
            stringSem = params[2];

            //  Log.e("data : ",UserName+dpt+ email_ID);
            ResultSet rs = null;

            try {
//         Check the connection or not
                Connection con = connectionClass.CONN();
                if (con == null) {
                    Toast.makeText(context, "Server Error... Please try again later", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("InElse", "else");
                    CallableStatement statement = null;
                    try {
                        statement = con.prepareCall("{ call sp_view(?,?,?,?)}");

                        statement.setString(1, stringEnrollmentNo);
                        statement.setString(2, stringcourse);
                        statement.setString(3, stringSem);

                        //mode sharedProcedure
                        if (stringRecordType.equalsIgnoreCase("Course wise")) {
                            statement.setInt(4, 1);

                        } else if (stringRecordType.equalsIgnoreCase("Semester wise")) {
                            statement.setInt(4, 2);

                        } else if (stringRecordType.equalsIgnoreCase("Student wise")) {
                            statement.setInt(4, 3);
                        }
                        statement.execute();
                        rs = statement.getResultSet();

                    } catch (Exception e) {
                        Log.e("EXCEPTION", e.toString());
                    }
                    return rs;
                }
            } catch (Exception ex) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return rs;
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            progressDialog.dismiss();

            try {
                while (rs.next()) {
                    stringResultSetSession = rs.getString("session");
                    stringResultSetEnrollmentNo = rs.getString("EnrolmmentNo");
                    stringResultSetStud_Name = rs.getString("Stud_Name");
                    stringResultSetTPresent = rs.getString("TPresent");
                    stringResultSetTAbsent = rs.getString("TAbsent");
                    stringResultSetPPresent = rs.getString("PPresent");
                    stringResultSetPAbsent = rs.getString("PAbsent");

                    stringResultSetSessionList.add(stringResultSetSession);
                     stringResultSetEnrollmentNoList.add(stringResultSetEnrollmentNo);
                     stringResultSetStud_NameList.add(stringResultSetStud_Name);
                     stringResultSetTPresentList.add(stringResultSetTPresent);
                     stringResultSetTAbsentList.add(stringResultSetTAbsent);
                     stringResultSetPPresentList.add(stringResultSetPPresent);
                     stringResultSetPAbsentList.add(stringResultSetPAbsent);


                    Log.e("ATTENDENCE",stringResultSetSession+" "+stringResultSetEnrollmentNo+" "+stringResultSetStud_Name+" "+
                            stringResultSetTPresent+" "+stringResultSetTAbsent+" "+stringResultSetPPresent+" "+stringResultSetPAbsent);
                }

                tableInit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null)
                        rs.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

