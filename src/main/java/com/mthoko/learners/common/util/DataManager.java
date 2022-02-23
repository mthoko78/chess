package com.mthoko.learners.common.util;

import com.mthoko.learners.domain.address.Address;
import com.mthoko.learners.exception.ApplicationException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;

import static com.mthoko.learners.common.util.MyConstants.print;

public class DataManager {
    public static String getFileText(Path path, boolean addLineBreaks) {
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(new File(path.toString(), "UTF-8")));
            BufferedReader in = new BufferedReader(is);
            StringBuilder data = new StringBuilder();
            char[] cbuf = new char[1024];
            int len;
            while ((len = in.read(cbuf)) > 0) {
                String str = new String(cbuf, 0, len);
                data.append(str);
            }
            is.close();
            in.close();
            return data.toString();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public static void deleteAllFilesInDir(Path dir) {
        File file = new File(dir.toString());
        if (dir != null && file.isDirectory()) {
            for (File path : file.listFiles()) {
                path.delete();
            }
        }
    }

    public static String getFileText(Path path) {
        return getFileText(path, false);
    }

    public static String getURLData(String webAddress) throws Exception {
        return getURLData(webAddress, false);
    }

    public static String getURLData(String webAddress, boolean addLineBreaks) throws Exception {
        URL url = new URL(webAddress);
        URLConnection openConnection = url.openConnection();
        openConnection.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        InputStream is = openConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader in = new BufferedReader(isr);

        StringBuilder data = new StringBuilder();
        char[] cbuf = new char[1024];
        int len;
        while ((len = in.read(cbuf)) > 0) {
            String str = new String(cbuf, 0, len);
            data.append(str);
        }
        is.close();
        in.close();
        if (addLineBreaks)
            return data.toString().replaceAll("\n", " ").trim();
        return data.toString().trim();
    }

    @SuppressWarnings("unused")
    private static boolean writeDataToFile(String filename, String data, boolean overwriteExistingFile) {
        File path = new File(filename);
        if (overwriteExistingFile && path.exists())
            return false;
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(data.getBytes());
            outputStream.close();
            return true;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public static int uploadFile(String sourceFileUri, final String upLoadServerUri) throws IOException {
        final String fileName = sourceFileUri;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            print("Error: Source File not exist :" + fileName);
            return 0;

        } else {
            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            String boundary = "*****";
            // Open a HTTP connection to the URL
            HttpURLConnection conn = getHttpURLConnection(fileName, url, boundary);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            writeData(fileName, fileInputStream, boundary, dos);

            // Responses from the server (code and message)
            processServerResponse(fileName, conn);
            // close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
            return conn.getResponseCode();

        }
    }

    private static void processServerResponse(String fileName, HttpURLConnection conn) throws IOException {
        System.out
                .println("uploadFile: HTTP Response is : " + conn.getResponseMessage() + ": " + conn.getResponseCode());

        if (conn.getResponseCode() == 200) {
            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n" + fileName;
            print("info: " + msg);
        }
    }

    private static void writeData(String fileName, FileInputStream fileInputStream, String boundary,
                                  DataOutputStream dos) throws IOException {
        int maxBufferSize = 1 * 1024 * 1024;
        int bytesAvailable;
        int bufferSize;
        byte[] buffer;
        int bytesRead;
        String lineEnd = "\r\n";
        String twoHyphens = "--" + boundary + lineEnd;
        dos.writeBytes(twoHyphens);
        dos.writeBytes(
                "Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

        dos.writeBytes(lineEnd);

        // create a buffer of maximum size

        bufferSize = Math.min(fileInputStream.available(), maxBufferSize);
        buffer = new byte[bufferSize];

        // read file and write it into form...
        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {

            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        }

        // send multipart form data necesssary after file data...
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
    }

    public static HttpURLConnection getHttpURLConnection(String fileName, URL url, String boundary) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true); // Allow Inputs
        conn.setDoOutput(true); // Allow Outputs
        conn.setUseCaches(false); // Don't use a Cached Copy
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("enctype", "multipart/form-data");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        conn.setRequestProperty("uploaded_file", fileName);
        conn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        return conn;
    }

    public static Address retrieveAddress(double latitude, double longitude) {
        Address address = null;
        try {
            String url = getGeoLocUrl(latitude, longitude);
            String urlData = getURLData(url);
            address = retrieveAddress(urlData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
        return address;
    }

    public static String getGeoLocUrl(double latitude, double longitude) {
        return getGeoLocUrl(String.valueOf(latitude), String.valueOf(longitude));
    }

    public static String getGeoLocUrl(String latitude, String longitude) {
        final String latLong = (latitude + ", " + longitude);
        String apiKey = "YVT9TNokVXCI5A7TaC88zcTYEzwLVz5A";
        String apiUrl = "http://open.mapquestapi.com/geocoding/v1/reverse";
        return apiUrl + "?key=" + apiKey + "&location=" + latLong +
                "&includeRoadMetadata=true&includeNearestIntersection=true";
    }

    public static Address retrieveAddress(String addressString) throws JSONException {
        JSONObject jsonLocation = getJsonLocation(addressString);
        String country = null;
        String state = null;
        String city = null;
        String postalCode = null;
        String street = null;

        if (jsonLocation.has("adminArea1Type")) {
            country = jsonLocation.getString("adminArea1");
        }
        if (jsonLocation.has("adminArea3Type")) {
            state = jsonLocation.getString("adminArea3");
        }
        if (jsonLocation.has("adminArea5Type")) {
            city = jsonLocation.getString("adminArea5");
        }
        if (jsonLocation.has("postalCode")) {
            postalCode = jsonLocation.getString("postalCode");
        }
        if (jsonLocation.has("street")) {
            street = jsonLocation.getString("street");
        }
        Address address = new Address();
        address.setCountry(country);
        address.setState(state);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setStreet(street);
        return address;
    }

    public static JSONObject getJsonLocation(String address) throws JSONException {
        JSONObject jsonAddress = new JSONObject(address);
        return jsonAddress.getJSONArray("results").getJSONObject(0).getJSONArray("locations").getJSONObject(0);
    }
}