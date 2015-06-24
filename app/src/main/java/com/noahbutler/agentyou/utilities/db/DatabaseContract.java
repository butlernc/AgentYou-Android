package com.noahbutler.agentyou.utilities.db;

import android.os.AsyncTask;
import android.util.Log;

import com.noahbutler.agentyou.data.DatabaseStream;
import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.threads.Messenger;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Noah Butler on 3/8/2015.
 */
public class DatabaseContract extends AsyncTask<String, Object , Object> {

    private static String URL_NEW_AGENT    = "http://www.schasta.com/agentyou/new_agent.php";
    private static String URL_NEW_FACEDATA = "http://www.schasta.com/agentyou/new_faceData.php";
    private static String URL_GET_FACEDATA   = "http://www.schasta.com/agentyou/load_all_faceData.php";
    public boolean isFinished;

    @Override
    protected Object doInBackground(String... strings) {
        isFinished = false;
        if(strings[0].contains("sendNewUser")) {
            sendNewUser(strings[1], strings[2], strings[3]);
        }else if(strings[0].contains("sendNewFaceData")) {
            sendNewFaceData(strings[1], strings[2], strings[3]);
        }else if(strings[0].contains("getFaceData")) {
            getFaceData();
        }

        return null;
    }

    public void sendNewUser(String agent, String email, String pass) {
        Log.d("NEWUSER", "Sending: " + agent + ", email: " + email + ", and pass: " + pass + " to server!");

        //preparing post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("agent", agent));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("pass", pass));

        ServiceHandler serviceClient = new ServiceHandler();

        String json = serviceClient.makeServiceCall(URL_NEW_AGENT,
                ServiceHandler.POST, params);

        Log.d(Statics.LOG, "Creating agent on database > " + json);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                boolean error = jsonObj.getBoolean("error");
                // checking for error node in json
                if (!error) {
                    // new category created successfully
                    Log.e("Agent added", "> " + jsonObj.getString("output"));
                } else {
                    Log.e("Add Prediction Error: ",
                            "> " + jsonObj.getString("output"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "JSON data error!");
        }

        /* update the text on the current screen the user is seeing to tell them their user profile
        was saved successfully. */
        Messenger messenger = new Messenger();
        messenger.sendSaveResults("Agent profile created successfully! You may proceed.");

    }

    public void sendNewFaceData(String email, String data, String bufferLength) {
        Log.d(Statics.LOG, "Sending face data for: " + email);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("data", data));
        params.add(new BasicNameValuePair("bufferLength", bufferLength));

        ServiceHandler serviceClient = new ServiceHandler();

        String json = serviceClient.makeServiceCall(URL_NEW_FACEDATA,
                ServiceHandler.POST, params);

        Log.d(Statics.LOG, "Creating agent's face data on database > " + json);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                boolean error = jsonObj.getBoolean("error");
                // checking for error node in json
                if (!error) {
                    // new category created successfully
                    Log.e("Agent face data added", "> " + jsonObj.getString("output"));
                } else {
                    Log.e("Add Prediction Error: ",
                            "> " + jsonObj.getString("output"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "JSON data error!");
        }

        /* update the text on the current screen the user is seeing to tell them their user profile
        was saved successfully. */
        Messenger messenger = new Messenger();
        messenger.sendSaveResults("Agent face data saved successfully! You may proceed.");
    }

    public void getFaceData() {
        ServiceHandler serviceClient = new ServiceHandler();

        String json = serviceClient.makeServiceCall(URL_GET_FACEDATA,
                ServiceHandler.GET);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                boolean error = jsonObj.getBoolean("error");
                // checking for error node in json
                if (!error) {//success
                    /* update the text on the current screen the user is seeing to tell them their user profile
                    was saved successfully. */
                    Messenger messenger = new Messenger();
                    messenger.sendFaceData(json);

                    Log.e("Agent face data added", "> " + jsonObj.getString("output"));

                    String key;
                    int index = 0;
                    HashMap<String,ArrayList<String>> emailFaceData = new HashMap<>();
                    ArrayList<String> emailKeys = new ArrayList<>();
                    //while((key = jsonObj.getString("email_" + index)) != null) {

                    for(int i = 0; i < 1; i++) {
                        key = jsonObj.getString("email_" + index);
                        String data = jsonObj.getString("facedata_" + index);
                        String bufferLength = jsonObj.getString("bufferLength_" + index);
                        /* add face data to arrays in our app that we can use else where with ease */
                        ArrayList<String> imageData = new ArrayList<>();
                        imageData.add(data);
                        imageData.add(bufferLength);
                        emailFaceData.put(key, imageData);

                        /* save keys to a separate array for easy access */
                        emailKeys.add(key);

                        index++;
                    }

                    DatabaseStream.EmailFaceData = emailFaceData;
                    DatabaseStream.emailKeys = emailKeys;
                } else {
                    Log.e("Add Prediction Error: ",
                            "> " + jsonObj.getString("output"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "JSON data error!");
        }



    }
}
