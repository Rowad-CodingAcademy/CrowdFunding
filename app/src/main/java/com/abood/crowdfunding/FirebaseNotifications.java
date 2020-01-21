package com.abood.crowdfunding;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
//import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


class FirebaseNotifications extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... voids) {


        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AIzaSyCJzH_QZx1e2MCRdBvgBvpAh6X4aB_DV6Q");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("to", LoginActivity.token);


            JSONObject info = new JSONObject();
            info.put("title", "New Campaign");   // FirebaseNotifications title
            info.put("body", FirebaseAuth.getInstance().getUid()+"Added a new Campaign"); // FirebaseNotifications body
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        }
        catch (Exception e)
        {
            Log.d("Error",""+e);
        }


        return null;
    }
}