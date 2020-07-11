package com.mthoko.mobile.util;

import android.content.Context;
import android.util.Log;

import com.mthoko.mobile.exception.ApplicationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mthoko on 30 Sep 2018.
 */

public class HttpManager {
    Context context;

    public HttpManager(Context context) {
        this.context = context;
    }

    public static String getData(RequestPackage requestPackage) {
        Log.e("REQUEST PARAMS: ", ">>>" + requestPackage.getParams() + "<<<");
        BufferedReader reader = null;
        String uri = requestPackage.getUrl();
        if (requestPackage.getMethod().equals("GET")) {
            uri += "?" + requestPackage.getEncodedParams();
        }
        String response = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestPackage.getMethod());
            if (requestPackage.getCookie() != null) {
                con.setRequestProperty("Cookie", requestPackage.getCookie());
            }
            if (requestPackage.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer =
                        new OutputStreamWriter(con.getOutputStream());
                String encodedParams = requestPackage.getEncodedParams();
                writer.write(encodedParams);
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
            reader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            response = sb.toString();
            if (uri.contains("monitor.php")) {
                response = response.substring(sb.indexOf(" ")).trim();
            }
            Log.e("RAW RESPONSE", "" + response);
            return response;
        } catch (IOException e) {
            throw new ApplicationException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new ApplicationException(e);
                }
            }
        }
    }
}