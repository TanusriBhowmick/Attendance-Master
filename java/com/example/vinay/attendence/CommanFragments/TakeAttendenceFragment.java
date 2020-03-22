package com.example.vinay.attendence.CommanFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.VU;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


public class TakeAttendenceFragment extends Fragment implements View.OnClickListener{
    View view;
    Button btnTakeAttendence;
    AppCompatEditText etAdminEmployeeID,
            etAdminSubject;
    TextInputLayout TxtLayoutCourseId;
    AppCompatTextView TxtAdminTimeFrom,TxtAdminTimeTo,TxtAdminCourseId;

    String stringEmployeeID, stringCourseId,stringDate,stringCourseName,stringSem,
            stringDpt,stringTimeFrom, stringTimeTo,stringBatch,stringShift;
    RadioButton radioTheory,radioPractical;
    String stringRadio_type="";
    Spinner spnCourseName,spnDpt,spnSem,spnBatch,spnShift;
    Context context;
    View viewSpace;
    LinearLayout llShift;
    TextView dateTxt;
    ConnectionClass connectionClass = null;
    String[] courseIdArray = {"course Id","IT501E","CM505E","CM411E","CM409E","EM401E","FS501E",
                                "CM415E","CM406E","CM403E","CM407E"};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_take_attendence, container, false);
        context =getActivity();
        connectionClass = new ConnectionClass();
        initialize();
        etAdminEmployeeID.setText(ACU.MySP.getFromSP(context,ACU.MySP.PROFILE_EMPLOYEE_ID,""));
        etAdminEmployeeID.setEnabled(false);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initialize() {

        viewSpace = view.findViewById(R.id.view);
        dateTxt = view.findViewById(R.id.Txtdate);

        spnShift = (view.findViewById(R.id.spnShift));
        llShift = (view.findViewById(R.id.llShift));

        btnTakeAttendence = view.findViewById(R.id.btnTakeAttendence);
        btnTakeAttendence.setOnClickListener(this);

        spnCourseName= view.findViewById(R.id.spnCourseName);
        spnDpt= view.findViewById(R.id.spnDepartment);
        spnSem= view.findViewById(R.id.spn_sem);
        spnBatch= view.findViewById(R.id.spn_Batch);

        etAdminEmployeeID= view.findViewById(R.id.etAdminEmployeeID);
        TxtAdminTimeFrom= view.findViewById(R.id.TxtTimeFrom);
        TxtAdminTimeTo= view.findViewById(R.id.TxtTimeTo);
        TxtAdminCourseId= view.findViewById(R.id.TxtAdminCourseId);
        TxtLayoutCourseId= view.findViewById(R.id.TxtLayoutCourseId);

        radioTheory= view.findViewById(R.id.radio_theory);
        radioPractical= view.findViewById(R.id.radio_practical);

        radioTheory.setOnClickListener(this);
        radioPractical.setOnClickListener(this);
        TxtAdminTimeFrom.setOnClickListener(this);
        TxtAdminTimeTo.setOnClickListener(this);

        spnDpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spnDpt.getItemAtPosition(position).equals("Civil Engineering") ||
                        spnDpt.getItemAtPosition(position).equals("Mechanical Engineering") ||
                        spnDpt.getItemAtPosition(position).equals("ETC Engineering")){
                    Log.e("VISIBLE","visible");
                    llShift.setVisibility(View.VISIBLE);
                    spnShift.setVisibility(View.VISIBLE);
                }else {
                    Log.e("VISIBLE", "gone");
                    llShift.setVisibility(View.GONE);
                    spnShift.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnCourseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TxtAdminCourseId.setText(courseIdArray[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDateandTime = sdf.format(new Date());
        dateTxt.setText(currentDateandTime);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTakeAttendence:
                if(Validate()) {

                    if (!stringRadio_type.equalsIgnoreCase("")) {

                        stringEmployeeID= etAdminEmployeeID.getText().toString().trim();
                        stringTimeFrom= TxtAdminTimeFrom.getText().toString().trim();
                        stringTimeTo= TxtAdminTimeTo.getText().toString().trim();
                        stringCourseId= TxtAdminCourseId.getText().toString().trim();
                        stringCourseName= spnCourseName.getSelectedItem().toString().trim();
                        stringDpt= spnDpt.getSelectedItem().toString().trim();
                        stringSem= spnSem.getSelectedItem().toString().trim();
                        stringBatch= spnBatch.getSelectedItem().toString().trim();
                        stringDate= dateTxt.getText().toString().trim();
                        stringShift= spnShift.getSelectedItem().toString().trim();


                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_DATE,stringDate);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_TYPE, stringRadio_type);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_EMPLOYEEID,stringEmployeeID);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_DEPARTMENT,stringDpt);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_COURSENAME,stringCourseName);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_COURSEID,stringCourseId);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_SEMESTER,stringSem);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_TIMEFROM,stringTimeFrom);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_TIMETO,stringTimeTo);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_BATCH,stringBatch);
                        ACU.MySP.saveSP(context,ACU.MySP.Take_ATTENDENCE_SHIFT,stringShift);

                        TakeAttendenceAsync takeAttendenceAsync = new TakeAttendenceAsync();
                            takeAttendenceAsync.execute(stringEmployeeID,stringTimeFrom,stringTimeTo,stringCourseId,stringCourseName,
                                                               stringDpt,stringSem,stringDate,stringBatch,stringShift);

                       /* Fragment fragment = new StudentListFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();*/

                    }else {
                        Toast.makeText(context, "Please select Type", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.radio_theory:
                stringRadio_type = "theory";
                viewSpace.setVisibility(View.GONE);
                spnBatch.setVisibility(View.GONE);
                Toast.makeText(context, stringRadio_type,Toast.LENGTH_SHORT).show();
                break;

            case R.id.radio_practical:
                stringRadio_type = "practicals";
                viewSpace.setVisibility(View.VISIBLE);
                spnBatch.setVisibility(View.VISIBLE);
                Toast.makeText(context, stringRadio_type,Toast.LENGTH_SHORT).show();
                break;

            case R.id.TxtTimeFrom:
                dialogTimePickerLight("from");
                break;

            case R.id.TxtTimeTo:
                dialogTimePickerLight("to");
                break;
        }
    }

    public boolean Validate() {
        if (VU.isEmpty(etAdminEmployeeID)) {
            etAdminEmployeeID.setError("Please Enter Employee Id");
            etAdminEmployeeID.requestFocus();
            return false;
        }
        else if(spnDpt.getSelectedItemId() == 0){
            Toast.makeText(context, "please select Department", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(stringRadio_type == "practicals"){
            if(spnBatch.getSelectedItemId() == 0) {
                Toast.makeText(context, "please select Batch", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else if(spnSem.getSelectedItemId() == 0){
            Toast.makeText(context, "please select Semester", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(spnCourseName.getSelectedItemId() == 0){
            Toast.makeText(context, "please select Course name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void dialogTimePickerLight(final String Txt) {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                if(Txt =="from") {
                    TxtAdminTimeFrom.setText(hourOfDay + " : " + minute);
                }else if(Txt == "to"){
                    TxtAdminTimeTo.setText(hourOfDay + " : " + minute);
                }
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }


    public class TakeAttendenceAsync extends AsyncTask<String,String,ResultSet> {

        String stringEmployeeID = "";
        String stringTimeFrom = "";
        String stringTimeTo = "";
        String stringCourseId = "";
        String stringCourseName = "";
        String stringDpt = "";
        String stringSem = "";
        String stringDate = "";
        String stringBatch = "";
        String stringShift = "";
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected ResultSet doInBackground(String... params) {

            stringEmployeeID = params[0];
            stringTimeFrom  = params[1];
            stringTimeTo  = params[2];
            stringCourseId  = params[3];
            stringCourseName  = params[4];
            stringDpt = params[5];
            stringSem  = params[6];
            stringDate  = params[7];
            stringBatch  = params[8];
            stringShift  = params[9];
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
                        statement = con.prepareCall("{ call sp_FetchStudent(?,?,?,?,?)}");

                        statement.setString( 1, stringDpt);
                        statement.setString(2, stringSem);

                        if(stringRadio_type.equalsIgnoreCase("theory")){
                            statement.setString(3, "Batch A");

                            //mode sharedProcedure
                            statement.setInt(5, 1);
                        }else{
                            statement.setString(3, stringBatch);
                            //mode sharedProcedure
                            statement.setInt(5, 2);
                        }

                        if(stringDpt.equals("Civil Engineering") ||
                                stringDpt.equals("Mechanical Engineering") ||
                                stringDpt.equals("ETC Engineering")){
                            statement.setString(4, stringShift);

                        }else {
                            statement.setString(4, "Shift 1");
                        }
                        statement.execute();
                       /* statement.setString( 1, null);
                        statement.setString(2, null);
                        statement.setString(3, null);
                        statement.setString(4, null);
                        statement.setInt(5, 3);
                        statement.execute();*/

                        rs = statement.getResultSet();

                    }catch (Exception e){
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
            ArrayList<String> studentList = new ArrayList<>();
            ArrayList<String> enrollmentNoList = new ArrayList<>();

            ArrayList<byte[]> fingerArray = null;  //for six finger
            ArrayList<ArrayList<byte[]>> fingersArray = new ArrayList<>();  //for group of six finger
            byte[] finger2= null;
            byte[] finger3 = null;
            byte[] finger4 = null;
            byte[] finger5 = null;
            byte[] finger6 = null;
            Log.e("rs",rs.toString());

            try {
                while (rs.next()) {
                    byte[] finger1 = null;
                    fingerArray = new ArrayList<>();
                    String studentName = rs.getString("Stud_Name");
                    String enrollmentNo = rs.getString("EnrolmmentNo");
                //    byte[] bytes = Convert.FromBase64(stringInBase64);

                     finger1 = (byte[]) (rs.getBytes("Thumb1"));
                    finger2 = rs.getBytes("Thumb2");
                    finger3 = rs.getBytes("Thumb3");
                    finger4 = rs.getBytes("Thumb4");
                    finger5 = rs.getBytes("Thumb5");
                    finger6 = rs.getBytes("Thumb6");

                    Log.e("StudName", studentName);
                    Log.e("finger1",finger1.toString());
                    Log.e("finger2",finger2.toString());
                    Log.e("finger3",finger3.toString());
                    Log.e("finger4",finger4.toString());
                    Log.e("finger5",finger5.toString());
                    Log.e("finger6",finger6.toString());

                     fingerArray.add(finger1);
                    fingerArray.add(finger2);
                    fingerArray.add(finger3);
                    fingerArray.add(finger4);
                    fingerArray.add(finger5);
                    fingerArray.add(finger6);

                    Log.e("FINGERARRAY",String.valueOf(fingerArray));

                   studentList.add(studentName);
                   enrollmentNoList.add(enrollmentNo);
                  fingersArray.add(fingerArray);

                    Log.e("FINGERsARRAY",String.valueOf(fingersArray));
                }

                Log.e("StudName", String.valueOf(studentList));
                Log.e("EnrollmentNo", String.valueOf(enrollmentNoList));


               ACU.MySP.saveArrayList(context,ACU.MySP.STUDENT_LIST,studentList);
               ACU.MySP.saveArrayList(context,ACU.MySP.ENROLLMENT_NO_LIST,enrollmentNoList);
                ACU.MySP.saveArraysBytesList(context,ACU.MySP.FINGERS_LIST,fingersArray);

                  Fragment fragment = new StudentListFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

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
