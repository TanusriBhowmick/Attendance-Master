package com.example.vinay.attendence.CommanFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.Utils.text_to_speech;
import com.example.vinay.attendence.adapters.StudentListAdapter;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StudentListFragment extends Fragment implements MFS100Event {
    View view;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private StudentListAdapter studentListAdapter;
    private ArrayList<String> studentList;
    private ArrayList<String> EnrollmentNoList;
    private ArrayList<ArrayList<byte[]>> fingersList;
    private Context context;
    private ConnectionClass connectionClass;
    private Button  btnCapture;
    int timeout = 10000;
    MFS100 mfs100 = null;
    private boolean isCaptureRunning = false;
    private FingerData lastCapFingerData = null;
    private int integer_finger_value;
    CheckBox cbFastDetection;
    byte[] Verify_Template;
    int position = 0;
    int flag = 0;
    int attendenceStatus = 0;
    String EnrollmentNo = null, studentName = null, stringDate, stringRadio_type, stringEmployeeID, stringDpt,
            stringCourseName, stringCourseId, stringSem, stringTimeFrom, stringTimeTo, stringBatch, stringShift;

    private enum ScannerAction {
        Capture, Verify
    }

    ScannerAction scannerAction = ScannerAction.Capture;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_list, container, false);
        context = getActivity();
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }


        connectionClass = new ConnectionClass();
        studentList = new ArrayList<>();
        fingersList = new ArrayList<>();
        EnrollmentNoList = new ArrayList<>();

        stringDate = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_DATE, "");
        stringRadio_type = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_TYPE, "");
        stringEmployeeID = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_EMPLOYEEID, "");
        stringDpt = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_DEPARTMENT, "");
        stringCourseName = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_COURSENAME, "");
        stringCourseId = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_COURSEID, "");
        stringSem = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_SEMESTER, "");
        stringTimeFrom = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_TIMEFROM, "");
        stringTimeTo = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_TIMETO, "");
        stringBatch = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_BATCH, "");
        stringShift = ACU.MySP.getFromSP(getActivity(), ACU.MySP.Take_ATTENDENCE_SHIFT, "");

        Log.e("stringDate", stringDate);
        Log.e("stringRadio_type", stringRadio_type);
        Log.e("stringEmployeeID", stringEmployeeID);
        Log.e("stringDpt", stringDpt);
        Log.e("stringCourseName", stringCourseName);
        Log.e("stringCourseId", stringCourseId);
        Log.e("stringSem", stringSem);
        Log.e("stringTimeFrom", stringTimeFrom);
        Log.e("stringTimeTo", stringTimeTo);
        Log.e("stringBatch", stringBatch);
        Log.e("stringShift", stringShift);


        studentList = ACU.MySP.getArrayList(context, ACU.MySP.STUDENT_LIST);
        fingersList = ACU.MySP.getArraysBytesList(context, ACU.MySP.FINGERS_LIST);
        EnrollmentNoList = ACU.MySP.getArrayList(context, ACU.MySP.ENROLLMENT_NO_LIST);
        Log.e("STUDENT_LIST", studentList.toString());
        Log.e("FINGERS_LIST", fingersList.toString());
        Log.e("ENROLLMENT_NO_LIST", EnrollmentNoList.toString());

        initializeUI();

        return view;
    }


    private void initializeUI() {
        cbFastDetection = view.findViewById(R.id.checkbox);
        btnCapture = view.findViewById(R.id.capture);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.frgmentStudentListRecycler);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        studentListAdapter = new StudentListAdapter(getActivity(), studentList);
        recyclerView.setAdapter(studentListAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);
                studentListAdapter = new StudentListAdapter(getActivity(), studentList);
                recyclerView.setAdapter(studentListAdapter);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    Toast.makeText(context, "Please keep your Finger on a device", Toast.LENGTH_SHORT).show();
                    // text_to_speech.speech(getActivity(), "Please keep your Finger on a device");
                    StartSyncCapture();
                }
            }
        });

    }

    @Override
    public void onStart() {
        if (mfs100 == null) {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(getActivity());
        } else {
            InitScanner();
        }
        super.onStart();
    }

    public void onStop() {
        UnInitScanner();
        super.onStop();
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                text_to_speech.speech(getActivity(), "Device Connected Successfully");
                //  SetTextOnUIThread("Init success");
               /* String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
                SetLogOnUIThread(info);*/
            }
        } catch (Exception ex) {
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                text_to_speech.speech(getActivity(), "Device removed");
                SetTextOnUIThread("Uninit Success");

                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }


    private void SetTextOnUIThread(final String str) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Snackbar snackbar = Snackbar
                        .make(view, str, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void StopCapture() {
        try {
            mfs100.StopAutoCapture();
        } catch (Exception e) {
            SetTextOnUIThread("Error");
        }
    }

    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        int ret;
        if (!hasPermission) {
            text_to_speech.speech(getActivity(), "Permission denied");
            SetTextOnUIThread("Permission denied");
            return;
        }
        if (vid == 1204 || vid == 11279) {
            if (pid == 34323) {
                ret = mfs100.LoadFirmware();
                if (ret != 0) {
                    SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                } else {
                    SetTextOnUIThread("Load firmware success");
                }
            } else if (pid == 4101) {
                String key = "Without Key";
                ret = mfs100.Init();
                if (ret == 0) {
                    showSuccessLog(key);
                } else {
                    SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                }

            }
        }
    }

    private void showSuccessLog(String key) {
        text_to_speech.speech(getActivity(), "Device Connected successfully");
        SetTextOnUIThread("Init success");

    }

    @Override
    public void OnDeviceDetached() {
        UnInitScanner();
        text_to_speech.speech(getActivity(), "Device removed");
        SetTextOnUIThread("Device removed");
    }

    @Override
    public void OnHostCheckFailed(String err) {
        try {
            // SetLogOnUIThread(err);
            Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }


    private void StartSyncCapture() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    final FingerData fingerData = new FingerData();
                    integer_finger_value = mfs100.AutoCapture(fingerData, timeout, cbFastDetection.isChecked());
                    Log.e("finger_value", "" + integer_finger_value);
                    if (integer_finger_value != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(integer_finger_value));
                        text_to_speech.speech(getActivity(), mfs100.GetErrorMsg(integer_finger_value));
                    } else {
                        lastCapFingerData = fingerData;
                        /*final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);*/
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Verify_Template = new byte[fingerData.ISOTemplate().length];
                                System.arraycopy(fingerData.ISOTemplate(), 0, Verify_Template, 0,
                                        fingerData.ISOTemplate().length);
                                //  imgFinger1.setImageBitmap(bitmap);
                                //   text_to_speech.speech(getActivity(), "fingerprint captured successfully");
                                // SetTextOnUIThread("fingerprint captured successfully ");
                                CheckFingerPrint(Verify_Template);

                            }
                        });


                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("  Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    private void CheckFingerPrint(byte[] verify_Template) {

        int ret = 0;
        try {
            for (int i = 0; i < studentList.size(); i++) {
                ArrayList<byte[]> fingerList = fingersList.get(i);
                for (int j = 0; j < fingerList.size(); j++) {
                    //       for (int k=0;k<)
                    byte[] finger = fingerList.get(j);
                    ret = mfs100.MatchISO(finger, verify_Template);
                    if (ret < 0) {
                        SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
                    } else {
                        if (ret >= 1400) {
                            flag =1;
                            position = i;
                            showAttendence();

                            // SetTextOnUIThread(studentList.get(i));

                            break;
                            // SetTextOnUIThread("Finger matched with score: " + ret);
                        }
                    }
                }
            }

            //  count =0;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //for present passing 1  and for absent null to database
    public void showAttendence() {

        Log.e("Position", String.valueOf(position));
        Log.e("flag", String.valueOf(flag));

        //     attendenceStatus.add(position,1 );
        if (flag ==1) {

            studentName = studentList.get(position);
            EnrollmentNo = EnrollmentNoList.get(position);
            attendenceStatus = 1;
            Log.e("EnrollmentNo", EnrollmentNo);
            Log.e("StudentName", studentName);
            Log.e("AttendenceStatus", String.valueOf(attendenceStatus));
            text_to_speech.speech(getActivity(), "Thank You");
            SetTextOnUIThread("Finger matched with score: " + studentName);
            UpdateAttendenceAsync updateAttendenceAsync = new UpdateAttendenceAsync();
            updateAttendenceAsync.execute();
            flag =0;
        }else if(flag ==0) {
         //   text_to_speech.speech(getActivity(), "Please try again");
            SetTextOnUIThread("Finger not matched...Please try again" );
        }
    }


    public class UpdateAttendenceAsync extends AsyncTask<String, String, ResultSet> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected ResultSet doInBackground(String... params) {

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
                        Date date=new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
                        statement = con.prepareCall("{ call sp_Attendance(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                        statement.setString(1, EnrollmentNo);
                        statement.setString(2, stringEmployeeID);
                        statement.setString(3, stringCourseId);
                        statement.setString(4, stringCourseName);
                        statement.setString(5, stringSem);
                        statement.setString(6, stringShift);
                        statement.setString(7, stringBatch);
                        statement.setString(8, stringDate);
                        statement.setString(9, stringTimeFrom);
                        statement.setString(10, stringTimeTo);
                        statement.setString(11, stringRadio_type);
                        statement.setInt(12, attendenceStatus);
                        statement.setString(13, studentName);
                        statement.setString(14, stringDpt);
                        statement.setInt(15, 1);
                        statement.execute();


                      //  rs = statement.getResultSet();
                    } catch (Exception e) {
                        Log.e("EXCEPTION", e.toString());
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(context, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            progressDialog.dismiss();
            String msg = null;

         /*   try {
                if (rs.next()) {
                    msg = rs.getString("Msg");

                }
                Log.e("MSG", msg);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null)
                        rs.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }
}

