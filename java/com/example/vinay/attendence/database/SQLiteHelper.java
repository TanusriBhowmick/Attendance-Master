package com.example.vinay.attendence.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DataBaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLiteQueries.CREATE_profile_TABLE);
        db.execSQL(SQLiteQueries.CREATE_STUDENT_REG_TABLE);

    }

    /*@Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA automatic_index = off;");
        }
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseConstants.TableNames.TBL_PROFILE);
        db.execSQL("DROP TABLE  IF EXISTS " + DataBaseConstants.TableNames.TBL_STUDENT_REG);
        onCreate(db);
    }
}