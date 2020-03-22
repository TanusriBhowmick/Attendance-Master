package com.example.vinay.attendence.AdminFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vinay.attendence.AdminActivities.AttendenceActivity;
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


public class AdminAttendenceFragment extends Fragment  {

    private RecyclerView recyclerView;
    private AdapterGridTwoLine mAdapter;
    private CardView viewRecordCard;
    View view;
    Fragment fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_attendence, container, false);
        initialize();
        viewRecordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACU.MySP.saveSP(getActivity(), ACU.MySP.FRAGMENT_NAME,"ViewRecords");
                startActivity(new Intent(getActivity(), AttendenceActivity.class));
            }
        });

        return view;
    }


    public void initialize(){
        recyclerView = (RecyclerView) view.findViewById(R.id.AttendenceRecyclerView);
        viewRecordCard = view.findViewById(R.id.cardViewRecords);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        List<Image> items = DataGenerator.getImageDate(getActivity(),"Attendence");
        //set data and list adapter
        mAdapter = new AdapterGridTwoLine(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridTwoLine.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Image obj, int position) {

                if (position == 0) {
                    ACU.MySP.saveSP(getActivity(), ACU.MySP.FRAGMENT_NAME,"TakeAttendence");
                    startActivity(new Intent(getActivity(), AttendenceActivity.class));
                } else if (position == 1) {
                    ACU.MySP.saveSP(getActivity(), ACU.MySP.FRAGMENT_NAME,"SpecialAttendence");
                    startActivity(new Intent(getActivity(), AttendenceActivity.class));
                }

            }
        });
    }

}
