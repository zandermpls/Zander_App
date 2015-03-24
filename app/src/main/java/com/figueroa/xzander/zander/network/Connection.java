package com.figueroa.xzander.zander.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xzander on 10/22/14.
 */
public class Connection {

    static String TAG = "Network Data, Zander";
    public static String testingUrl;

    // test network connection
    public static Boolean connectionStatus(Context context){
        // if there is a connection, comm will = true
        Boolean comm = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null){
            if (networkInfo.isConnected()){
                // connection exists, show type in Log
                Log.i(TAG,"Connection Type =" + networkInfo.getTypeName());
                comm = true;
            }
        }
        return comm;
    }

    public static String getResponse(URL url){
        // testing for connection
        testingUrl = "http://www.rawartists.org/zander";
        String response = "";

        // open connection to testingUrl and see if accessible
        try {

            URLConnection connection = url.openConnection();
            BufferedInputStream bufferedInput = new BufferedInputStream(connection.getInputStream());
            byte[] bytesContext = new byte[1024];
            int readBytes = 0;
            // append data information being called for
            StringBuffer bufferResponse = new StringBuffer();
            // track bufferedInput process
            while((readBytes = bufferedInput.read(bytesContext)) != -1){
                response = new String(bytesContext, 0, readBytes);
                bufferResponse.append(response);
            }
            response = bufferResponse.toString();
            Log.i(TAG, response);
        } catch (IOException exception){
            response = "An error occurred - unable to read network data info.";
            Log.e(TAG, "something went wrong", exception);
        }

        return response;
    }

}
