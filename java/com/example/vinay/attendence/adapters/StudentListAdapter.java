package com.example.vinay.attendence.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinay.attendence.R;

import org.json.JSONArray;

import java.util.ArrayList;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private Context ctx;
    JSONArray array;
    ArrayList<String> studentList;

    public StudentListAdapter(Context ctx, ArrayList<String> studentList){
        this.ctx =ctx;
        this.array =array;
        this.studentList =studentList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView NameTxt;
        public ImageView StatusImg ;

        public MyViewHolder(View view) {
            super(view);
            NameTxt =   view.findViewById(R.id.nameTxt);
          //  StatusImg =  view.findViewById(R.id.statusImg);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.NameTxt.setText(studentList.get(position));
          //  myViewHolder.StatusImg.setText("p");

        }
    }

    @Override
    public int getItemCount() {
        // return array.length();
        return studentList.size();
    }

}
