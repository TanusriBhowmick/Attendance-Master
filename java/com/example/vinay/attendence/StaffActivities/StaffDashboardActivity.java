package com.example.vinay.attendence.StaffActivities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinay.attendence.CommanFragments.TakeAttendenceFragment;
import com.example.vinay.attendence.LoginActivity;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.StaffFragments.StaffDashboardFragment;
import com.example.vinay.attendence.CommanFragments.ViewRecordFragment;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.AppState;

public class StaffDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Context context;
    private ImageButton staff_attendance_btn,view_record_btn,logout_btn;
    private FloatingActionButton staffDashboardFloatBtn;
    private TextView staffAttendanceTxt,viewRecordTxt;
    private LinearLayout ViewRecordLL,StaffAttendanceLL;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
        context = StaffDashboardActivity.this;


        initToolbar();
        initializeUI();

        //  loading the default fragment
        loadFragment(new StaffDashboardFragment());
        staffDashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendance");
    }

    private void initializeUI() {
        logout_btn = findViewById(R.id.logout);
        staff_attendance_btn = findViewById(R.id.staff_attendance_button);
        view_record_btn = findViewById(R.id.view_record_button);
        staffDashboardFloatBtn = findViewById(R.id.staffDashboardBtn);
        staffAttendanceTxt = findViewById(R.id.staff_attendance_txt);
        viewRecordTxt = findViewById(R.id.view_record_txt);

        ViewRecordLL = findViewById(R.id.viewRecordLL);
        StaffAttendanceLL = findViewById(R.id.staffAttendanceLL);


        staffDashboardFloatBtn.setOnClickListener(this);
        view_record_btn.setOnClickListener(this);
        staff_attendance_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.staff_attendance_button:

                fragment = new TakeAttendenceFragment();
                staffDashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                staff_attendance_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));
                staffAttendanceTxt.setTextColor(getResources().getColor(R.color.deep_orange_400));

                view_record_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                viewRecordTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                loadFragment(fragment);

                break;

            case R.id.view_record_button:

                fragment = new ViewRecordFragment();
                staffDashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                view_record_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));
                viewRecordTxt.setTextColor(getResources().getColor(R.color.deep_orange_400));

                staff_attendance_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                staffAttendanceTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                loadFragment(fragment);

                break;

            case R.id.staffDashboardBtn:

                fragment = new StaffDashboardFragment();
                staffDashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));

                view_record_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                viewRecordTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                staff_attendance_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                staffAttendanceTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                loadFragment(fragment);

                break;

            case R.id.logout:
                    warninglogoutDiaglog();
                break;
        }
    }

    //  loading fragment
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.staff_fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void warninglogoutDiaglog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.logout_diaglog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btndiagcancle = (Button) dialog.findViewById(R.id.warning_diag_btnCancle);

        //add clickable
        final Button btndiagsubmit = (Button) dialog.findViewById(R.id.warning_diag_btnSubmit);

        btndiagcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        btndiagsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences myPrefs;
                myPrefs = getSharedPreferences("MY",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                AppState.getSingleInstance().setLoggingOut(true);
                Log.d("Logout", "Now log out and start the activity login");
                Intent intent = new Intent(StaffDashboardActivity.this,
                        LoginActivity.class);
                ACU.MySP.setSPBoolean(context, ACU.MySP.LOGIN_STATUS, false);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
