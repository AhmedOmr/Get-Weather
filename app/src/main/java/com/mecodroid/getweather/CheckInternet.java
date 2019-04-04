package com.mecodroid.getweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class CheckInternet {
    Context context;
    public CheckInternet(Context context){
        this.context = context;
    }
    public boolean Is_Connecting(){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
   if (manager != null){
       NetworkInfo networkInfo =  manager.getActiveNetworkInfo();
       if (networkInfo != null && networkInfo.isConnected()){
           return true;
       }
   }
    return false;
    }
}
