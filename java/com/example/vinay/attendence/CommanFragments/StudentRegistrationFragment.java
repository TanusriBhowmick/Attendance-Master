package com.example.vinay.attendence.CommanFragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.ConnectionClass;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.VU;
import com.example.vinay.attendence.Utils.text_to_speech;
import com.example.vinay.attendence.database.DataBaseHelper;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import java.io.ByteArrayOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentRegistrationFragment extends Fragment implements View.OnClickListener,MFS100Event {
    private String[] array_sem;
    Spinner spnSem,spnDpt,spnShift,spnBatch;
    View view;
    ImageView imgFinger1, imgFinger2,imgFinger3,imgFinger4,imgFinger5,imgFinger6;
    Button btnStopCap,btnCap1,btnCap2,btnCap3,btnCap4,btnCap5,btnCap6,btnstdRegst;
    TextView lblMessage;
    CheckBox cbFastDetection;
    LinearLayout llShift;
    private int integer_finger_value;
    private ArrayList<byte[]> fingerList;
    AppCompatEditText edtStdName,edtEnrollNo,edtEmail,edtMb;
    Context context;
    ConnectionClass connectionClass;

    String stringName,stringDepartment,stringEnrollmentNo,stringSemester,
            stringEmail,stringMobileNo,stringBatch,stringShift = null;


    private enum ScannerAction {
        Capture, Verify
    }
    byte[] Enroll_Template;
    byte[] Verify_Template;
    byte[] a,b,c,d,e,f;
    private FingerData lastCapFingerData = null;
    ScannerAction scannerAction = ScannerAction.Capture;

    int timeout = 10000;
    MFS100 mfs100 = null;

    private boolean isCaptureRunning = false;


    public StudentRegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        array_sem = getResources().getStringArray(R.array.sem);
        fingerList= new ArrayList<>();
        view =inflater.inflate(R.layout.fragment_student_registration, container, false);
        context = getActivity();
        connectionClass = new ConnectionClass();
        initialize();

        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void initialize() {
        spnSem = (view.findViewById(R.id.spn_sem));
        spnDpt = (view.findViewById(R.id.spnDepartment));
        spnShift = (view.findViewById(R.id.spnShift));
        spnBatch = (view.findViewById(R.id.spnBatch));
        llShift = (view.findViewById(R.id.llShift));

        edtStdName = (view.findViewById(R.id.stdName));
        edtEnrollNo = (view.findViewById(R.id.stdEnrollNo));
        edtEmail = (view.findViewById(R.id.stdEmail));
        edtMb = (view.findViewById(R.id.stdMb));



        lblMessage = view.findViewById(R.id.lblMessage);

        imgFinger1 = view.findViewById(R.id.imgFinger1);
        imgFinger2 = view.findViewById(R.id.imgFinger2);
        imgFinger3 = view.findViewById(R.id.imgFinger3);
        imgFinger4 = view.findViewById(R.id.imgFinger4);
        imgFinger5 = view.findViewById(R.id.imgFinger5);
        imgFinger6 = view.findViewById(R.id.imgFinger6);

        btnStopCap = view.findViewById(R.id.btnstopCap);
        btnCap1 = view.findViewById(R.id.btnFinger1);
        btnCap2 = view.findViewById(R.id.btnFinger2);
        btnCap3 = view.findViewById(R.id.btnFinger3);
        btnCap4 = view.findViewById(R.id.btnFinger4);
        btnCap5 = view.findViewById(R.id.btnFinger5);
        btnCap6 = view.findViewById(R.id.btnFinger6);
        btnstdRegst = view.findViewById(R.id.btnStuRegister);

        btnStopCap.setOnClickListener(this);
        btnCap1.setOnClickListener(this);
        btnCap2.setOnClickListener(this);
        btnCap3.setOnClickListener(this);
        btnCap4.setOnClickListener(this);
        btnCap5.setOnClickListener(this);
        btnCap6.setOnClickListener(this);
        btnstdRegst.setOnClickListener(this);
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

        cbFastDetection = view.findViewById(R.id.cbFastDetection);

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
                text_to_speech.speech(getActivity(),"Device Connected Successfully");
                SetTextOnUIThread("Init success");
               /* String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
                SetLogOnUIThread(info);*/
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            SetTextOnUIThread("Init failed, unhandled exception");
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                text_to_speech.speech(getActivity(),"Device removed");
                SetTextOnUIThread("Uninit Success");

                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }


    private void SetTextOnUIThread(final String str) {

        lblMessage.post(new Runnable() {
            public void run() {
                lblMessage.setText(str);
            }
        });
    }

   @Override
   public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinger1: {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    text_to_speech.speech(getActivity(), "Please keep your Finger on a device");
                    StartSyncCapture("finger 1");
                }
                break;
            }

            case R.id.btnFinger2: {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    text_to_speech.speech(getActivity(), "Please keep your Finger again on a device");
                    StartSyncCapture("finger 2");
                }

                break;
            }
            case R.id.btnFinger3: {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    text_to_speech.speech(getActivity(), "Please keep your Finger again on a device");
                    StartSyncCapture("finger 3");
                }
                break;
            }
            case R.id.btnFinger4: {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    text_to_speech.speech(getActivity(), "Please keep your Finger again on a device");
                    StartSyncCapture("finger 4");
                }
                break;
            }
            case R.id.btnFinger5: {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    text_to_speech.speech(getActivity(), "Please keep your Finger again on a device");
                    StartSyncCapture("finger 5");
                }
                break;
            }
            case R.id.btnFinger6: {
                scannerAction = ScannerAction.Capture;
                if (!isCaptureRunning) {
                    text_to_speech.speech(getActivity(), "Please keep your Finger again on a device");
                    StartSyncCapture("finger 6");
                }
                break;
            }
            case R.id.btnstopCap: {
            //    StopCapture();
                int ret = mfs100.MatchISO(a, b);
                if (ret < 0) {
                    SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
                } else {
                    if (ret >= 1400) {
                        SetTextOnUIThread("Finger matched with score: " + ret);
                    } else {
                        SetTextOnUIThread("Finger not matched, score: " + ret);
                    }
                }

                ArrayList<byte[]> list = ACU.MySP.getArrayByteList(context,ACU.MySP.FINGER_LIST);
                Log.e("LIST",list.toString());
                break;
            }
            case R.id.btnStuRegister: {
                if (Validate()) {

                    stringName = edtStdName.getText().toString().trim();
                    stringDepartment = spnDpt.getSelectedItem().toString().trim();
                    stringEnrollmentNo = edtEnrollNo.getText().toString().trim();
                    stringSemester = spnSem.getSelectedItem().toString().trim();
                    stringEmail = edtEmail.getText().toString().trim();
                    stringMobileNo = edtMb.getText().toString().trim();
                    stringBatch = spnBatch.getSelectedItem().toString().trim();
                    stringShift = spnShift.getSelectedItem().toString().trim();


                    if (VU.isConnectingToInternet(context)) {
                        StudentResistrationAsync studentResistrationAsync = new StudentResistrationAsync(stringName, stringDepartment,
                                stringEnrollmentNo, stringSemester, stringEmail,
                                stringMobileNo, stringBatch,stringShift);
                        studentResistrationAsync.execute();
                    }
                }
            }
            break;

        }
   }

    private void StopCapture() {
        try {
            mfs100.StopAutoCapture();
        } catch (Exception e) {
            SetTextOnUIThread("Error");
        }
    }


    private void StartSyncCapture(final String finger) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    final FingerData fingerData = new FingerData();
                   integer_finger_value = mfs100.AutoCapture(fingerData, timeout, cbFastDetection.isChecked());
                    Log.e("finger_value", ""+integer_finger_value);

                    if (integer_finger_value != 0) {
                        SetTextOnUIThread(mfs100.GetErrorMsg(integer_finger_value));
                        text_to_speech.speech(getActivity(),mfs100.GetErrorMsg(integer_finger_value));
                    } else {
                        lastCapFingerData = fingerData;
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finger.equalsIgnoreCase("finger 1")) {

                                    a = new byte[fingerData.ISOTemplate().length];
                                    System.arraycopy(fingerData.ISOTemplate(), 0, a, 0,
                                            fingerData.ISOTemplate().length);
                                    fingerList.add(a);
                                    imgFinger1.setImageBitmap(bitmap);

                                }else if (finger.equalsIgnoreCase("finger 2")) {
                                    b = new byte[fingerData.ISOTemplate().length];
                                    System.arraycopy(fingerData.ISOTemplate(), 0, b, 0,
                                            fingerData.ISOTemplate().length);
                                    fingerList.add(b);
                                    imgFinger2.setImageBitmap(bitmap);

                                }else if (finger.equalsIgnoreCase("finger 3")) {

                                    c = new byte[fingerData.ISOTemplate().length];
                                    System.arraycopy(fingerData.ISOTemplate(), 0, c, 0,
                                            fingerData.ISOTemplate().length);
                                    fingerList.add(c);
                                    imgFinger3.setImageBitmap(bitmap);
                                }else if (finger.equalsIgnoreCase("finger 4")) {

                                    d = new byte[fingerData.ISOTemplate().length];
                                    System.arraycopy(fingerData.ISOTemplate(), 0, d, 0,
                                            fingerData.ISOTemplate().length);
                                    fingerList.add(d);
                                    imgFinger4.setImageBitmap(bitmap);
                                }else if (finger.equalsIgnoreCase("finger 5")) {

                                    e = new byte[fingerData.ISOTemplate().length];
                                    System.arraycopy(fingerData.ISOTemplate(), 0, e, 0,
                                            fingerData.ISOTemplate().length);
                                    fingerList.add(e);
                                    imgFinger5.setImageBitmap(bitmap);
                                }else if (finger.equalsIgnoreCase("finger 6")) {

                                    f = new byte[fingerData.ISOTemplate().length];
                                    System.arraycopy(fingerData.ISOTemplate(), 0, f, 0,
                                            fingerData.ISOTemplate().length);
                                    fingerList.add(f);
                                    imgFinger6.setImageBitmap(bitmap);
                                }
                            }
                        });

                        text_to_speech.speech(getActivity(), "fingerprint captured successfully");
                        SetTextOnUIThread("Capture Success");

                  //      SetData2(fingerData);
                    }
                } catch (Exception ex) {
                    SetTextOnUIThread("  Error");
                } finally {
                    ACU.MySP.saveArrayByteList(context,ACU.MySP.FINGER_LIST,fingerList);
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

   /* public void SetData2(FingerData fingerData) {
        if (scannerAction.equals(ScannerAction.Capture)) {
            Enroll_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Enroll_Template, 0,
                    fingerData.ISOTemplate().length);
        } else if (scannerAction.equals(ScannerAction.Verify)) {
            Verify_Template = new byte[fingerData.ISOTemplate().length];
            System.arraycopy(fingerData.ISOTemplate(), 0, Verify_Template, 0,
                    fingerData.ISOTemplate().length);
            int ret = mfs100.MatchISO(Enroll_Template, Verify_Template);
            if (ret < 0) {
                SetTextOnUIThread("Error: " + ret + "(" + mfs100.GetErrorMsg(ret) + ")");
            } else {
                if (ret >= 1400) {
                    SetTextOnUIThread("Finger matched with score: " + ret);
                } else {
                    SetTextOnUIThread("Finger not matched, score: " + ret);
                }
            }
        }

    }*/

    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        int ret;
        if (!hasPermission) {
            text_to_speech.speech(getActivity(),"Permission denied");
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
        text_to_speech.speech(getActivity(),"Device Connected successfully");
        SetTextOnUIThread("Init success");

    }

    @Override
    public void OnDeviceDetached() {
        UnInitScanner();
        text_to_speech.speech(getActivity(),"Device removed");
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

    public boolean Validate() {
        if (VU.isEmpty(edtStdName)) {
            edtStdName.setError("Please Enter Name");
            edtStdName.requestFocus();
            return false;
        } else if (spnDpt.getSelectedItemId() == 0) {
            Toast.makeText(getActivity(), "please select department ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (VU.isEmpty(edtEnrollNo)) {
            edtEnrollNo.setError("Please Enter valid Enrollment No");
            edtEnrollNo.requestFocus();
            return false;
        } else if (spnSem.getSelectedItemId() == 0) {
            Toast.makeText(getActivity(), "please select Semester ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (VU.isEmailId(edtEmail)) {
            edtEmail.setError("Please Enter valid Email Id");
            edtEmail.requestFocus();
            return false;
        } else if (VU.isEmpty(edtMb)) {
            edtMb.setError("Please Enter valid Mobile No");
            edtMb.requestFocus();
            return false;
        }
        return true;
    }


    public class StudentResistrationAsync extends AsyncTask<String,String,ResultSet> {

        String stringName = "";
        String stringDepartment = "";
        String stringEnrollmentNo = "";
        String stringSemester = "";
        String stringEmail = "";
        String stringMobileNo = "";
        String stringBatch = "";
        String stringShift = "";
        ProgressDialog progressDialog;


        StudentResistrationAsync(String stringName,String stringDepartment,String stringEnrollmentNo,
                                 String  stringSemester,String stringEmail,
                                 String stringMobileNo, String stringBatch,String stringShift){
            this.stringName = stringName;
            this.stringDepartment = stringDepartment;
            this.stringEnrollmentNo = stringEnrollmentNo;
            this.stringSemester = stringSemester;
            this.stringEmail = stringEmail;
            this.stringMobileNo = stringMobileNo;
            this.stringBatch = stringBatch;
            this.stringShift = stringShift;

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    "ProgressDialog",
                    "Please Wait...");
        }

        @Override
        protected ResultSet doInBackground(String... params) {

            Log.e("data : ",stringEnrollmentNo);
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
                        statement = con.prepareCall("{ call sp_StudRegistration(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                        statement.setString(1, stringEnrollmentNo);
                        statement.setString(2, stringName);
                        statement.setString(3, stringEmail);
                        statement.setString(4, stringMobileNo);
                        statement.setString(5, stringDepartment);
                        statement.setString(6, stringSemester);
                        statement.setString(7, stringBatch);
                        statement.setString(8, stringShift);
                        statement.setBytes(9, a);
                        statement.setBytes(10,b );
                        statement.setBytes(11, c);
                        statement.setBytes(12, d);
                        statement.setBytes(13, e);
                        statement.setBytes(14, f);
                        statement.setInt(15, 2);
                        statement.execute();


                        statement = con.prepareCall("{ call sp_StudRegistration(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

                        statement.setString(1, stringEnrollmentNo);
                        statement.setString(2, stringName);
                        statement.setString(3, stringEmail);
                        statement.setString(4, stringMobileNo);
                        statement.setString(5, stringDepartment);
                        statement.setString(6, stringSemester);
                        statement.setString(7, stringBatch);
                        statement.setString(8, stringShift);
                        statement.setBytes(9, null);
                        statement.setBytes(10,null );
                        statement.setBytes(11, null);
                        statement.setBytes(12, null);
                        statement.setBytes(13, null);
                        statement.setBytes(14, null);
                        statement.setInt(15, 4);
                        statement.execute();
                        rs = statement.getResultSet();

                    }catch (Exception e){
                        Log.e("EXCEPTION", e.toString());
                    }
                //    return rs;
                }
            } catch (Exception ex) {
                Toast.makeText(context, "Something went wrong..Please try again later", Toast.LENGTH_SHORT).show();
            }
            return rs;
        }

        @Override
        protected void onPostExecute(ResultSet rs) {
            progressDialog.dismiss();
            try {
                if (rs.next()) {
                    final String msg = rs.getString("Msg");
                    Log.e("MSG",msg);

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Snackbar snackbar = Snackbar
                                    .make(view, msg, Snackbar.LENGTH_LONG);
                            snackbar.show();                        }
                    });
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
