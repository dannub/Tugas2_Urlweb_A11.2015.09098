package com.example.user.tugas2;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

/**
 * Created by User on 21/10/2017.
 */

public class HTMLResource extends AsyncTaskLoader<String> {
    private  String url_link;
    public HTMLResource(Context context, String url_link) {
        super(context);
        this.url_link = url_link;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        InputStream in;
        try {
            URL url = new URL(url_link);
            HttpURLConnection conect = (HttpURLConnection) url.openConnection();
            conect.setReadTimeout(10000);
            conect.setConnectTimeout(20000);
            conect.setRequestMethod("GET");
            conect.connect();

            in = conect.getInputStream();

            BufferedReader buff = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = buff.readLine())!= null){
                sb.append(line +"\n");
            }

            buff.close();
            in.close();

            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  "Error";
    }
}
