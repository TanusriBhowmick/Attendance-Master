package com.example.vinay.attendence.AdminActivities;

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

import com.example.vinay.attendence.AdminFragments.AdminAttendenceFragment;
import com.example.vinay.attendence.AdminFragments.AdminCreateUserFragment;
import com.example.vinay.attendence.AdminFragments.AdminDashboardFragment;
import com.example.vinay.attendence.LoginActivity;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.StaffActivities.StaffDashboardActivity;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.AppState;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageButton createUser_btn,attendence_btn,logout_btn;
    private FloatingActionButton dashboardFloatBtn;
    private TextView userTxt,AttendenceTxt;
    private LinearLayout AttendenceLL,CreateUserLL;
    private Context context;
    Fragment fragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        context = DashBoardActivity.this;
        initToolbar();
        initializeUI();

        //  loading the default fragment
        loadFragment(new AdminDashboardFragment());
        dashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));



    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Attendance");
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initializeUI(){
        logout_btn = findViewById(R.id.logout);
        createUser_btn = findViewById(R.id.user_button);
        attendence_btn = findViewById(R.id.attendence_button);
        dashboardFloatBtn = findViewById(R.id.dashboardBtn);
        userTxt = findViewById(R.id.user_txt);
        AttendenceTxt = findViewById(R.id.attendence_txt);

        AttendenceLL = findViewById(R.id.attendenceLL);
        CreateUserLL = findViewById(R.id.createUserLL);


        dashboardFloatBtn.setOnClickListener(this);
        attendence_btn.setOnClickListener(this);
        createUser_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.user_button:
                fragment = new AdminCreateUserFragment();
                dashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                createUser_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));
                userTxt.setTextColor(getResources().getColor(R.color.deep_orange_400));

                attendence_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                AttendenceTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                loadFragment(fragment);
                break;

            case  R.id.attendence_button:
                fragment = new AdminAttendenceFragment();
                dashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

                attendence_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));
                AttendenceTxt.setTextColor(getResources().getColor(R.color.deep_orange_400));

                createUser_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                userTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                loadFragment(fragment);
                break;

            case R.id.dashboardBtn:
                fragment = new AdminDashboardFragment();
                dashboardFloatBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.deep_orange_400)));

                attendence_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                AttendenceTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                createUser_btn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                userTxt.setTextColor(getResources().getColor(R.color.colorPrimary));

                loadFragment(fragment);
                break;

            case R.id.logout:
                warninglogoutDiaglog();
                break;

        }
    }

    //  loading fragment
    private boolean  loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
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
                Intent intent = new Intent(DashBoardActivity.this,
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
