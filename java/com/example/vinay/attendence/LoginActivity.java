package com.example.vinay.attendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vinay.attendence.AdminActivities.DashBoardActivity;
import com.example.vinay.attendence.StaffActivities.StaffDashboardActivity;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.Utils.VU;
import com.example.vinay.attendence.database.DataBaseHelper;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView admin, staff;
    Button btnLogin;
    Context context;
    String login_type = "",Emp_id = "0";
    DataBaseHelper db;
    AppCompatEditText etUsername, etPassword;
    ConnectionClass connectionClass;


    int timeout = 10000;
    MFS100 mfs100 = null;
    private boolean isCaptureRunning = false;
    private FingerData lastCapFingerData = null;
    private int integer_finger_value;
    CheckBox cbFastDetection;
    byte[] Verify_Template;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        connectionClass = new ConnectionClass();
        db = new DataBaseHelper(context);
       if(ACU.MySP.getSPBoolean(context,ACU.MySP.LOGIN_STATUS,false)){
            if(ACU.MySP.getFromSP(context,ACU.MySP.LOGIN_TYPE,"").equalsIgnoreCase("admin")) {
                startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                finish();
            }else if(ACU.MySP.getFromSP(context,ACU.MySP.LOGIN_TYPE,"").equalsIgnoreCase("staff")){
                startActivity(new Intent(LoginActivity.this, StaffDashboardActivity.class));
                finish();
            }
        }

        initToolbar();
        initComponent();


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initComponent() {
        final ImageView image = findViewById(R.id.image);
        admin = findViewById(R.id.adminTxt);
        staff = findViewById(R.id.staffTxt);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.loginBtn);
        admin.setOnClickListener(this);
        staff.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        final CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int min_height = ViewCompat.getMinimumHeight(collapsing_toolbar) * 2;
                float scale = (float) (min_height + verticalOffset) / min_height;
                image.setScaleX(scale >= 0 ? scale : 0);
                image.setScaleY(scale >= 0 ? scale : 0);
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adminTxt:
                login_type = "admin";
                ACU.MySP.saveSP(context, ACU.MySP.LOGIN_TYPE, login_type);
                admin.setBackgroundResource(R.drawable.gradient_background);
                staff.setBackgroundResource(R.drawable.round_border_right);
                break;
            case R.id.staffTxt:
                login_type = "staff";
                ACU.MySP.saveSP(context, ACU.MySP.LOGIN_TYPE, login_type);
                staff.setBackgroundResource(R.drawable.gradient_background);
                admin.setBackgroundResource(R.drawable.round_border_left);
                break;
            case R.id.loginBtn:
                String UID = etUsername.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
              /*  if (!login_type.equalsIgnoreCase("")) {
                   // if (VU.isConnectingToInternet(context))
                       // LoginCheck();
                    Emp_id = etUsername.getText().toString().trim();
                    Cursor res = DataBaseHelper.DBProfileData
                                    .getProfileData(Emp_id);

                    if(res.getCount() != 0) {
                        while (res.moveToNext()){
                      String EmpId= res.getString(res.getColumnIndex(DataBaseConstants.ProfileData.EMPLOYEE_ID));
                      String Pass= res.getString(res.getColumnIndex(DataBaseConstants.ProfileData.PASSWORD));
                      Log.e("CHECK",EmpId+Pass);
                            if(etPassword.getText().toString()
                                    .trim().equalsIgnoreCase(Pass)){
                                if (ACU.MySP.getFromSP(context, ACU.MySP.LOGIN_TYPE, "").equalsIgnoreCase("admin")) {
                                    ACU.MySP.setSPBoolean(context, ACU.MySP.LOGIN_STATUS, true);
                                    startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                                    finish();
                                }else if (ACU.MySP.getFromSP(context, ACU.MySP.LOGIN_TYPE, "").equalsIgnoreCase("staff")) {
                                    ACU.MySP.setSPBoolean(context, ACU.MySP.LOGIN_STATUS, true);
                                    startActivity(new Intent(LoginActivity.this, StaffDashboardActivity.class));
                                    finish();
                                }
                            }else{
                                Toast.makeText(context, "Password is wrong...Please Check ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(context, "EmployeeId is wrong..Please Check ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    View layout = getLayoutInflater().inflate(R.layout.toast_custom, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setTextColor(Color.BLACK);
                    text.setText("Please select Type");
                    CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
                    lyt_card.setCardBackgroundColor(getResources().getColor(R.color.amber_10));

                    Toast toast = new Toast(context);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }*/

                if(VU.isConnectingToInternet(context)){
                    if(!login_type.equalsIgnoreCase("")) {
                        DoLogin doLogin = new DoLogin();
                        doLogin.execute(UID, pass, login_type);
                    }else
                    {
                        Toast.makeText(context, "Please select Admin or Staff", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }



    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String Userid = "";
        String loginType = "";
        String Password = "";
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected String doInBackground(String... params) {

            Userid = params[0];
            Password = params[1];
            loginType = params[2];
            ResultSet rs = null;
            if(etUsername.getText().toString().trim().equals("")|| etPassword.getText().toString().trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                    try {
//         Check the connection or not
                        Connection con = connectionClass.CONN();
                        if (con == null) {
                            Toast.makeText(context, "Server Error... Please try again later", Toast.LENGTH_SHORT).show();
                        } else {
                            CallableStatement statement = con.prepareCall("{ call sp_Login(?,?,?)}");
                            statement.setString(1,loginType);
                            statement.setString(2,Userid);
                            statement.setString(3,Password);
                            Log.e("DATABASE",loginType+Password+Userid);
                            statement.execute();
                            rs = statement.getResultSet();
                            Log.e("rs",rs.toString());
                            if (rs.next()) {
                                // process result set
                                z = "Login successfull";
                                isSuccess=true;

                                ACU.MySP.saveSP(context,ACU.MySP.PROFILE_NAME,rs.getString("User_Name"));
                                ACU.MySP.saveSP(context,ACU.MySP.PROFILE_DEPARTMENT,rs.getString("Department"));
                                ACU.MySP.saveSP(context,ACU.MySP.PROFILE_EMPLOYEE_ID,rs.getString("Emp_ID"));
                                ACU.MySP.saveSP(context,ACU.MySP.PROFILE_MOBILE_NO,rs.getString("Contact_No"));
                                ACU.MySP.saveSP(context,ACU.MySP.PROFILE_EMAIL_ID,rs.getString("Email_ID"));

                                Log.e("DEPARTMENT",ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_NAME,""));
                                Log.e("DEPARTMENT",ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_DEPARTMENT,""));
                                Log.e("DEPARTMENT",ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_EMPLOYEE_ID,""));
                                Log.e("DEPARTMENT",ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_MOBILE_NO,""));


                            }
                            else{
                                z = "Invalid user and password";
                                isSuccess = false;
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        isSuccess = false;
                    }
                    finally {
                        try {
                            if(rs!=null)
                                rs.close();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            }
            return z;
        }
        @Override
        protected void onPostExecute(String r) {
            progressDialog.dismiss();
            if(isSuccess) {
                if (ACU.MySP.getFromSP(context, ACU.MySP.LOGIN_TYPE, "").equalsIgnoreCase("admin")) {
                    ACU.MySP.setSPBoolean(context, ACU.MySP.LOGIN_STATUS, true);
                    startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                    finish();
                }else if (ACU.MySP.getFromSP(context, ACU.MySP.LOGIN_TYPE, "").equalsIgnoreCase("staff")) {
                    ACU.MySP.setSPBoolean(context, ACU.MySP.LOGIN_STATUS, true);
                    startActivity(new Intent(LoginActivity.this, StaffDashboardActivity.class));
                    finish();
                }


            }else
                Toast.makeText(context,z, Toast.LENGTH_SHORT).show();

        }
    }





}
