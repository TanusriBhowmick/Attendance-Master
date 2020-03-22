package com.example.vinay.attendence.AdminFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vinay.attendence.AdminActivities.DashBoardActivity;
import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.ModelClasses.ModelProfileData;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.VU;
import com.example.vinay.attendence.database.DataBaseHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminCreateUserFragment extends Fragment implements View.OnClickListener{
    View view;
    EditText etUserName, etUserEmployeeID, etUserEmail,
            etUserMobileNo,etUserPassword, etUserRepassword;

    String stringUserName, stringUserEmployeeID=" ", stringUserDepartment, stringUserEmail,
            stringUserMobileNo, stringUserPassword, stringUserRepassword,stringradioType="";

    RadioButton radioStaff,radioAdmin;
    Button btnRegister;
    Spinner spnDpt;
    public Context context;
    ConnectionClass connectionClass;
    String msg = null ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        connectionClass = new ConnectionClass();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_create_user, container, false);
        context = view.getContext();


        initialize();
        return view;
    }

    private void initialize() {
        btnRegister= view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        spnDpt= view.findViewById(R.id.spnDepartment);

        radioStaff= view.findViewById(R.id.radio_staff);
        radioAdmin= view.findViewById(R.id.radio_admin);
        radioStaff.setOnClickListener(this);
        radioAdmin.setOnClickListener(this);


        etUserName= view.findViewById(R.id.userName);
        etUserEmployeeID= view.findViewById(R.id.userEmployeeID);
        etUserEmail= view.findViewById(R.id.userEmail);
        etUserMobileNo= view.findViewById(R.id.userMobileNo);
        etUserPassword= view.findViewById(R.id.userPassword);
        etUserRepassword= view.findViewById(R.id.userRePassword);

    }

    public boolean Validate() {
        if (VU.isEmpty(etUserName)) {
            etUserName.setError("Please Enter Name");
            etUserName.requestFocus();
            return false;
        }
        else if (spnDpt.getSelectedItemId() ==0) {
            Toast.makeText(context, "please select department ", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (VU.isEmpty(etUserEmployeeID)) {
            etUserEmployeeID.setError("Please Enter valid Employee Id");
            etUserEmployeeID.requestFocus();
            return false;
        }
        else if (VU.isEmailId(etUserEmail)) {
            etUserEmail.setError("Please Enter valid Email Id");
            etUserEmail.requestFocus();
            return false;
        }
        else if (VU.isEmpty(etUserMobileNo)) {
            etUserMobileNo.setError("Please Enter valid Mobile No");
            etUserMobileNo.requestFocus();
            return false;
        }
        else if (VU.isEmpty(etUserPassword)) {
            etUserPassword.setError("Please Enter valid password");
            etUserPassword.requestFocus();
            return false;
        }
        else if (VU.isEmpty(etUserRepassword)) {
            etUserRepassword.setError("Please Retype your password");
            etUserRepassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if (!stringradioType.equalsIgnoreCase("")) {
                    if(Validate()) {
                        stringUserName =  etUserName.getText().toString().trim();
                        stringUserEmployeeID= etUserEmployeeID.getText().toString().trim();
                        stringUserEmail= etUserEmail.getText().toString().trim();
                        stringUserMobileNo= etUserMobileNo.getText().toString().trim();
                        stringUserPassword= etUserPassword.getText().toString().trim();
                        stringUserRepassword= etUserRepassword.getText().toString().trim();
                        stringUserDepartment = spnDpt.getSelectedItem(). toString().trim();

                        if(VU.isConnectingToInternet(context)) {
                            if (etUserPassword.getText().toString().trim().equalsIgnoreCase
                                    (etUserRepassword.getText().toString().trim())) {
                                CreateUserAsync createUserAsync = new CreateUserAsync(stringradioType, stringUserName, stringUserDepartment,
                                        stringUserEmployeeID, stringUserEmail,
                                        stringUserMobileNo, stringUserPassword,context);
                                createUserAsync.execute();
                            } else {
                                Toast.makeText(context, "Password not matched...please check", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Please select Type", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.radio_staff:
                stringradioType = "staff";
                Toast.makeText(context,stringradioType,Toast.LENGTH_SHORT).show();
                break;

            case R.id.radio_admin:
                stringradioType = "admin";
                Toast.makeText(context,stringradioType,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class CreateUserAsync extends AsyncTask<String,String,ResultSet> {
        String UserName = "";
        String UserType = "";
        String dpt = "";
        String emp_ID = "";
        String email_ID = "";
        String mobileNo = "";
        String password = "";
        Context context ;
        ProgressDialog progressDialog;

        CreateUserAsync(String UserType,String UserName,String dpt,String emp_ID,String email_ID,
                        String mobileNo,String password,Context context ){
            this.UserName = UserName;
            this.UserType = UserType;
            this.dpt = dpt;
            this.emp_ID = emp_ID;
            this.email_ID = email_ID;
            this.mobileNo = mobileNo;
            this.password = password;
            this.context = context;

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected ResultSet doInBackground(String... params) {

                  Log.e("data : ",UserName+dpt+ email_ID);
            ResultSet rs = null;

            try {
//         Check the connection or not
                Connection con = connectionClass.CONN();
                if (con == null) {
                    Toast.makeText(context, "Server Error... Please try again later", Toast.LENGTH_SHORT).show();

                } else {
                    Log.e("InElse","else");
                    CallableStatement statement = null;
                    try {
                        statement = con.prepareCall("{ call sp_UserRegistration(?,?,?,?,?,?,?,?)}");

                        statement.setString(1, UserType);
                        statement.setString(2, UserName);
                        statement.setString(3, dpt);
                        statement.setString(4, emp_ID);
                        statement.setString(5, email_ID);
                        statement.setString(6, mobileNo);
                        statement.setString(7, password);
                        statement.setInt(8, 2);
                        statement.execute();

                        statement = con.prepareCall("{ call sp_UserRegistration(?,?,?,?,?,?,?,?)}");

                        statement.setString(1, UserType);
                        statement.setString(2, UserName);
                        statement.setString(3, dpt);
                        statement.setString(4, emp_ID);
                        statement.setString(5, email_ID);
                        statement.setString(6, mobileNo);
                        statement.setString(7, password);
                        statement.setInt(8, 5);

                        statement.execute();
                        rs = statement.getResultSet();
                    }catch (Exception e){
                        Log.e("EXCEPTION", e.toString());
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(context, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(final ResultSet rs) {
            progressDialog.dismiss();
            try {
                if (rs.next()) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                msg = rs.getString("Msg");
                                Snackbar snackbar = Snackbar
                                    .make(view, msg, Snackbar.LENGTH_LONG);

                                snackbar.show();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                if (!  msg.equalsIgnoreCase("Employee Already Registered")){
                    etUserName.setText("");
                    etUserEmployeeID.setText("");
                    etUserEmail.setText("");
                    etUserMobileNo.setText("");
                    etUserPassword.setText("");
                    etUserRepassword.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
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
