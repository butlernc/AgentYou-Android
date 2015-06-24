package com.noahbutler.agentyou.utilities.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

import com.noahbutler.agentyou.data.Statics;

/**
 * Created by Noah Butler on 3/27/2015.
 */


public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;


            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);

                /* if there are params, set them */
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                /* execute our call to our server */
                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {

                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }

            InputStream inputStream;
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            /* convert our input stream into a string to return */
            return streamReader(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String streamReader(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            response = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error: " + e.toString());
        }

        return response;
    }
}
