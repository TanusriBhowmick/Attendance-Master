package com.example.vinay.attendence.AdminActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vinay.attendence.CommanFragments.SpecialAttendenceFragment;
import com.example.vinay.attendence.CommanFragments.TakeAttendenceFragment;
import com.example.vinay.attendence.CommanFragments.ViewRecordFragment;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class AttendenceActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView shareImg;
    private Context context;
    Fragment fragment = null;
    String fragmentName = "";
    String Fnamexls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        context = AttendenceActivity.this;
        initToolbar();

       // shareImg.setVisibility(View.GONE);
        fragmentName = ACU.MySP.getFromSP(context, ACU.MySP.FRAGMENT_NAME, "");
        openFragment();




    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // shareImg = (ImageView) findViewById(R.id.imgShare);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendance");
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void openFragment() {
        if(fragmentName.equalsIgnoreCase("TakeAttendence")){
            fragment = new TakeAttendenceFragment();
            loadFragment(fragment);
        }else if(fragmentName.equalsIgnoreCase("SpecialAttendence")){
            fragment = new SpecialAttendenceFragment();
            loadFragment(fragment);
        }else if(fragmentName.equalsIgnoreCase("ViewRecords")){
            fragment = new ViewRecordFragment();
              //   shareImg.setVisibility(View.VISIBLE);
            loadFragment(fragment);

        }
    }


    //  loading fragment
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment,"")
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

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
        String pattern = "dd_MM_yyyy_HH_mm ";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, new Locale("da", "DK"));

        String date = simpleDateFormat.format(new Date());
        System.out.println(date);

        Fnamexls = date + "_Excel.xls";
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
            Label label7 = new Label(5, 0, "Mean(T)");
            Label label8 = new Label(6, 0, "Present(P)");
            Label label9 = new Label(7, 0, "Absent(P)");
            Label label10 = new Label(8, 0, "Mean(P)");
            Label label11 = new Label(9, 0, "Total Avg(%)");
            try {
                for (int i = 1; i < 10; i++) {
                    Label enrollData = new Label(0, i, "enroll");
                    Label StudentData = new Label(1, i, "Student");
                    Label TotalSessionData = new Label(2, i, "session");
                    Label PresentTData = new Label(3, i, "present theory");
                    Label AbsentTData = new Label(4, i, "absent Theory");
                    Label MeanTData = new Label(5, i, "mean Theory");
                    Label PresentPData = new Label(6, i, "present prac");
                    Label AbsentPData = new Label(7, i, "absent prac");
                    Label MeanPData = new Label(8, i, "mean Prac");
                    Label TotalAvgData = new Label(9, i, "tota Avg");

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
}
