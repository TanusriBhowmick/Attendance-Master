package com.example.vinay.attendence.Utils;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created by admin on 1/31/2018.
 */

public class ConnectionClass extends NetworkConnectivityCheck {
    static String call="43.255.152.26", db="DB_Attendance", un="Atd_Sys", passwords="Jr73do9@";
    //static  String call="vivah111.mssql.somee.com", db="vivah111", un="tuluraut_SQLLogin_1", passwords="3ky4tbvm4m";
    Connection connect;
    static  NetworkConnectivityCheck connectivityCheck = new NetworkConnectivityCheck();

    ResultSet rs;
    @SuppressLint("NewApi")
    public static Connection  CONN() {
//        if (connectivityCheck.haveNetworkConnection()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn= null;
            String ConnURL= null;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnURL = "jdbc:jtds:sqlserver://" + call + ";"
                        + "databaseName=" + db + ";user=" + un + ";password="
                        + passwords + ";";
                conn = DriverManager.getConnection(ConnURL);
//            } catch (SQLException se) {
//                Log.e("ERRO", se.getMessage());
//            } catch (ClassNotFoundException e) {
//                Log.e("ERRO", e.getMessage());
            } catch (Exception e) {
                Log.e("ERRO", "Connection failed");
            }
            return conn;

//        }
//        else{
//
//           return  null;
//        }

    }

}

