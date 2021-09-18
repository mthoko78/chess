package com.mthoko.learners.common.util;

import com.mthoko.learners.exception.ApplicationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.mthoko.learners.common.util.MyConstants.print;

/**
 * Created by Mthoko on 30 Sep 2018.
 */

public class HttpManager {

    public HttpManager() {
    }

    public static String getData(RequestPackage requestPackage) {
        print("REQUEST PARAMS: " + requestPackage.getParams());
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
            print("RAW RESPONSE: " + response);
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