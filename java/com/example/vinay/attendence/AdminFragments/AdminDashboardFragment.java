package com.example.vinay.attendence.AdminFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinay.attendence.AdminActivities.AttendenceActivity;
import com.example.vinay.attendence.CommanFragments.AboutUsActivity;
import com.example.vinay.attendence.CommanFragments.StudentRegistrationFragment;
import com.example.vinay.attendence.CommanFragments.MyProfileFragment;
import com.example.vinay.attendence.LoginActivity;
import com.example.vinay.attendence.R;
import com.example.vinay.attendence.Utils.ACU;
import com.example.vinay.attendence.Utils.DataGenerator;
import com.example.vinay.attendence.Utils.Image;
import com.example.vinay.attendence.Utils.SpacingItemDecoration;
import com.example.vinay.attendence.Utils.Tools;
import com.example.vinay.attendence.Utils.VU;
import com.example.vinay.attendence.adapters.AdapterGridTwoLine;

import java.util.List;


public class AdminDashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterGridTwoLine mAdapter;
    private CardView aboutUsCard;
    Fragment fragment = null;


    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);
        initialize();
        aboutUsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
            }
        });
        return view;
    }

    public void initialize(){
        aboutUsCard = view.findViewById(R.id.cardAboutUs);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);



        List<Image> items = DataGenerator.getImageDate(getActivity(),"Dashboard");
        //set data and list adapter
        mAdapter = new AdapterGridTwoLine(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridTwoLine.OnItemClickListener() {

            @Override
            public void onItemClick(View view, Image obj, int position) {

                if (position == 0) {
                    fragment = new MyProfileFragment();
                    replaceFragment(fragment);
                } else if (position == 1) {
                    fragment = new StudentRegistrationFragment();
                    replaceFragment(fragment);
                }

            }
        });



    }

    //  loading fragment
    private boolean replaceFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

}
