package com.example.vinay.attendence.StaffFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vinay.attendence.CommanFragments.AboutUsActivity;
import com.example.vinay.attendence.CommanFragments.SpecialAttendenceFragment;
import com.example.vinay.attendence.CommanFragments.MyProfileFragment;
import com.example.vinay.attendence.CommanFragments.StudentRegistrationFragment;
import com.example.vinay.attendence.LoginActivity;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.VU;


public class StaffDashboardFragment extends Fragment implements View.OnClickListener {

    public StaffDashboardFragment() {
        // Required empty public constructor
    }
    View view;
    Fragment fragment= null;
    LinearLayout lyt_profile, lyt_attendance, lyt_about_us,lyt_stu_registration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_staff_dashboard, container, false);

        lyt_profile= view.findViewById(R.id.lyt_profile);
        lyt_attendance= view.findViewById(R.id.lyt_attendance);
        lyt_about_us= view.findViewById(R.id.lyt_about_us);
        lyt_stu_registration= view.findViewById(R.id.lyt_stuReg);

        lyt_profile.setOnClickListener(this);
        lyt_attendance.setOnClickListener(this);
        lyt_about_us.setOnClickListener(this);
        lyt_stu_registration.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lyt_profile:
                fragment = new MyProfileFragment();
                replaceFragment(fragment);
                break;
            case R.id.lyt_attendance:
                fragment = new SpecialAttendenceFragment();
                replaceFragment(fragment);
                break;

            case R.id.lyt_stuReg:
                fragment = new StudentRegistrationFragment();
                replaceFragment(fragment);
                break;
            case R.id.lyt_about_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;

        }
    }

    private boolean replaceFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.staff_fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

}
