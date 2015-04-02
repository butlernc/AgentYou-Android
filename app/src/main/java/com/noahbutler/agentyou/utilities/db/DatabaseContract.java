package com.noahbutler.agentyou.utilities.db;

import android.os.AsyncTask;
import android.util.Log;

import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.photo.AgentHashCompare;
import com.noahbutler.agentyou.utilities.threads.Messenger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noah Butler on 3/8/2015.
 */
public class DatabaseContract extends AsyncTask<String, Boolean , Boolean> {

    private static String URL_NEW_AGENT  = "http://agentyou.square7.ch/new_agent.php";
    private static String URL_GET_AGENTS = "http://agentyou.square7.ch/load_agents.php";
    public boolean isFinished;

    @Override
    protected Boolean doInBackground(String... strings) {
        isFinished = false;
        if(strings[0].contains("sendNewUser")) {
            sendNewUser(strings[1], strings[2]);
        }

        if(strings[0].contains("loadAgents")) {
            loadAgents(strings[1]);
            isFinished = true;
        }

        return false;
    }

    public void sendNewUser(String agent, String HASHID) {
        Log.d("NEWUSER", "Sending: " + agent + ", and HASHID: " + HASHID + ", to server!");

        //preparing post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("agent", agent));
        params.add(new BasicNameValuePair("hashid", HASHID));

        ServiceHandler serviceClient = new ServiceHandler();

        String json = serviceClient.makeServiceCall(URL_NEW_AGENT,
                ServiceHandler.POST, params);

        Log.d("Create Agent Request: ", "> " + json);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                boolean error = jsonObj.getBoolean("error");
                // checking for error node in json
                if (!error) {
                    // new category created successfully
                    Log.e("Agent added", "> " + jsonObj.getString("message"));
                } else {
                    Log.e("Add Prediction Error: ",
                            "> " + jsonObj.getString("message"));
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

    public void loadAgents(String hashCompare) {
        ServiceHandler serviceClient = new ServiceHandler();

        //preparing post params
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        String json = serviceClient.makeServiceCall(URL_GET_AGENTS,
                ServiceHandler.GET, params);

        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                Log.d("AGENTS", jsonArray.toString());

                AgentHashCompare agentHashCompare = new AgentHashCompare();
                Messenger messenger = new Messenger();

                int i = 0;
                while(!jsonArray.isNull(i)) {
                    String incoming = jsonArray.getJSONObject(i).getString("ASIH");
                    Log.d(Statics.LOG, "Number in Database: " + i + " Agent ID: " + incoming);
                    int hammingDistance = agentHashCompare.distance(hashCompare, incoming);
                    Log.d(Statics.LOG, "Hamming Distance: " + hammingDistance);
                    if(hammingDistance < 25) {
                        Log.d("HAM", "TRUE");

                        /* send back to waiting fragment */
                        messenger.sendHammingResults("Matched: " + jsonArray.getJSONObject(i).getString("NAME"));
                        return;
                    }
                    i++;
                }

                messenger.sendHammingResults("No match");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Log.e("JSON Data", "JSON data error!");
        }
    }
}
