package com.mthoko.mobile.util;

import com.mthoko.mobile.exception.ApplicationException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mthoko on 29 Sep 2018.
 */

public class RequestPackage {
    private String url;
    private String method; // method is set to GET by default
    private Map<String, String> params;// This will hold any values
    private String cookie;
    // that the server may require
    // e.g. product_id

    public RequestPackage(String method, String url) {
        this(method, url, new HashMap<String, String>());
    }

    public RequestPackage(String method, String url, Map<String, String> params) {
        this.method = method;
        this.url = url;
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        params.put(key, value); // adds a single value to the params member variable
    }

    // The method below is only called if the request method has been set to GET
    // GET requests sends data in the url and it has to be encoded correctly in
    // order
    // for the server to understand the request. This method encodes the data in the
    // params variable so that the server can understand the request

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        try {
            for (String key : params.keySet()) {
                String value = null;
                value = URLEncoder.encode(params.get(key), "UTF-8");

                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key + "=" + value);
            }
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }
        return sb.toString();
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}