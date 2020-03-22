package com.example.vinay.attendence.CommanFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.Utils.VU;
import com.example.vinay.attendence.database.DataBaseHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyProfileFragment extends Fragment {

    View view;
    Button btnEditProfile;
    EditText etProfileName, etProfileEmployeeID,
            etProfileMobileNo,etProfileEmailId;
    AppCompatTextView TxtDpt;

    String stringProfileName, stringProfileEmployeeID,
            stringProfileMobileNo,stringProfileEmailId,stringProfileDepartment;
    boolean update = false;
    Context context;
    private ConnectionClass connectionClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        context = getActivity();
        initialize();
        connectionClass = new ConnectionClass();
        TxtDpt.setText(ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_DEPARTMENT,""));
        etProfileEmailId.setText(ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_EMAIL_ID,""));
        etProfileEmployeeID.setText(ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_EMPLOYEE_ID,""));
        etProfileMobileNo.setText(ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_MOBILE_NO,""));
        etProfileName.setText(ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_NAME,""));

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (btnEditProfile.getText().toString().equals("Edit Profile")) {
                        Log.e("inside_edit", btnEditProfile.getText().toString());

                        etProfileName.setEnabled(true);
                      //  etProfileEmployeeID.setEnabled(true);
                        etProfileMobileNo.setEnabled(true);
                        etProfileEmailId.setEnabled(true);
                        btnEditProfile.setText("UPDATE Profile");
                    } else {

                        Log.e("inside", btnEditProfile.getText().toString());
                        if(Validate()){
                            etProfileName.setEnabled(false);
                            etProfileEmployeeID.setEnabled(false);
                            etProfileMobileNo.setEnabled(false);
                            etProfileEmailId.setEnabled(false);
                            btnEditProfile.setText("Edit Profile");

                            stringProfileName= etProfileName.getText().toString().trim();
                            stringProfileEmployeeID= etProfileEmployeeID.getText().toString().trim();
                            stringProfileMobileNo= etProfileMobileNo.getText().toString().trim();
                            stringProfileEmailId= etProfileEmailId.getText().toString().trim();

                                UpdateProfileAsync updateProfileAsync = new UpdateProfileAsync();
                                updateProfileAsync.execute(stringProfileName, stringProfileEmployeeID, stringProfileMobileNo, stringProfileEmailId);

                        }
                    }
                }
        });
        return view;
    }

    private void initialize() {
        btnEditProfile= view.findViewById(R.id.btnEditProfile);
        etProfileName= view.findViewById(R.id.etProfileName);
        etProfileEmployeeID= view.findViewById(R.id.etProfileEmployeeID);
        etProfileMobileNo= view.findViewById(R.id.etProfileMobileNo);
        etProfileEmailId= view.findViewById(R.id.etProfileEmailId);

        TxtDpt= view.findViewById(R.id.TxtDepartment);


        etProfileName.setEnabled(false);
        etProfileEmployeeID.setEnabled(false);
        etProfileMobileNo.setEnabled(false);
        etProfileEmailId.setEnabled(false);



    }

    public boolean Validate() {
        if (VU.isEmpty(etProfileName)) {
            etProfileName.setError("Please enter Name");
            etProfileName.requestFocus();
            return false;
        }else if (VU.isEmpty(etProfileEmployeeID)) {
            etProfileEmployeeID.setError("Please enter Employee Id");
            etProfileEmployeeID.requestFocus();
            return false;
        }else if (VU.isEmpty(etProfileMobileNo)) {
            etProfileMobileNo.setError("Please enter Mobile No");
            etProfileMobileNo.requestFocus();
            return false;
        }else if (VU.isEmailId(etProfileEmailId)) {
            etProfileEmailId.setError("Please enter valid  Email Id");
            etProfileEmailId.requestFocus();
            return false;
        }

        return  true;
    }

    public class UpdateProfileAsync extends AsyncTask<String,String,ResultSet> {

        String stringName = "";
        String stringEmployeeID = "";
        String stringMobileNo = "";
        String stringEmailId = "";
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected ResultSet doInBackground(String... params) {

            stringName = params[0];
            stringEmployeeID  = params[1];
            stringMobileNo  = params[2];
            stringEmailId  = params[3];

            //  Log.e("data : ",UserName+dpt+ email_ID);
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
                        statement = con.prepareCall("{ call sp_updateUser(?,?,?,?,?)}");

                        statement.setString(1, stringName);
                        statement.setString(2, stringEmployeeID);
                        statement.setString(3, stringMobileNo);
                        statement.setString(4, stringEmailId);
                        statement.setInt(5, 1);
                        statement.execute();

                        statement = con.prepareCall("{ call sp_updateUser(?,?,?,?,?)}");

                        statement.setString(1, stringName);
                        statement.setString(2, stringEmployeeID);
                        statement.setString(3, stringMobileNo);
                        statement.setString(4, stringEmailId);
                        statement.setInt(5, 2);
                        statement.execute();
                        rs = statement.getResultSet();

                    }catch (Exception e){
                        Log.e("EXCEPTION", e.toString());
                    }
                    return rs;
                }
            } catch (Exception ex) {
                Toast.makeText(context, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            progressDialog.dismiss();


            try {
                while (rs.next()) {
                    etProfileName.setText(rs.getString("UserName"));
                    etProfileEmployeeID.setText(rs.getString("EmpID"));
                    etProfileMobileNo.setText(rs.getString("MobNo"));
                    etProfileEmailId.setText(rs.getString("EmailID"));

                }
            }catch (SQLException e) {
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
