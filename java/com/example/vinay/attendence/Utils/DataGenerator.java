package com.example.vinay.attendence.Utils;

import android.content.Context;
import android.content.res.TypedArray;


import com.example.vinay.attendence.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("ResourceType")
public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }



   /* public static List<Integer> getNatureImages(Context ctx) {
        List<Integer> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.sample_images);
        for (int i = 0; i < drw_arr.length(); i++) {
            items.add(drw_arr.getResourceId(i, -1));
        }
        Collections.shuffle(items);
        return items;
    }*/




    public static List<Image> getImageDate(Context ctx,String FragmtName) {
        List<Image> items = new ArrayList<>();
        TypedArray drw_arr = null;
        String name_arr[] = null;
        if(FragmtName.equalsIgnoreCase("Dashboard")) {
            drw_arr = ctx.getResources().obtainTypedArray(R.array.DashBoardFragmt_images);
            name_arr = ctx.getResources().getStringArray(R.array.DashBoardFragmt_images_name);
        }

        if(FragmtName.equalsIgnoreCase("Attendence")) {
            drw_arr = ctx.getResources().obtainTypedArray(R.array.AttendenceFragmt_images);
            name_arr = ctx.getResources().getStringArray(R.array.AttendenceFragmt_images_name);
        }
        for (int i = 0; i < drw_arr.length(); i++) {
            Image obj = new Image();
            obj.image = drw_arr.getResourceId(i,-1);
            obj.name = name_arr[i];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        return items;
    }
}
