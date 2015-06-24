package com.noahbutler.agentyou.utilities.photo;

import android.util.Log;

import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.db.ServiceHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noah Butler on 4/14/2015.
 */
public class FaceLinker {

    private static final String API_KEY = "PLOBKE1Og8mshUBLVIHnXB2qa2f3p1Gdl61jsnT8btjdllfdBz";
    private static final String ALBUM_KEY = "b2d14c827cead395ececdbfc53db4e5ffcddf81f6841b083704082a1af733eb5";
    private static final String APP_ID = "21dd8967";
    private static final String GALLERY_ID = "AgentYou";

    /* http request params */
    private JSONObject json_parameters;

    /* response code from server call */
    private int responseCode;
    private String response;

    /* image directory on bluehost */
    private static final String BH_IMAGE_DIRECTORY = "http://www.schasta.com/agentyou/FaceAPIInterface/save_file.php";

    /* linker on bluehost */
    private static final String LINKER_ALBUM_TRAIN = "http://www.schasta.com/agentyou/FaceAPIInterface/album_learn.php";

    /* api links */
    private static final String RECOGNIZE_URL  = "https://lambda-face-recognition.p.mashape.com/recognize";
    private static final String VIEW_ALBUM_URL = "https://lambda-face-recognition.p.mashape.com/album?album=AgentYou&albumkey=b2d14c827cead395ececdbfc53db4e5ffcddf81f6841b083704082a1af733eb5";
    private static final String CREATE_ALBUM_URL = "https://lambda-face-recognition.p.mashape.com/album";

    public boolean createAlbum() {
        json_parameters = new JSONObject();

        try {

            json_parameters.put("album", "AgentYouV2");

            /* create our http post object */
            HttpPost httpPost = new HttpPost(CREATE_ALBUM_URL);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("X-Mashape-Key", API_KEY);
            httpPost.addHeader("Accept", "application/json");

            /* use the httpPost object and get our response from the api */
            response = doPostRequest(httpPost);

            Log.d("SENT", "async http client sent request to create album");
            Log.d("RESPONSE", "Response: " + response);

            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean detectFaceFromImage(String filePath) {
        return false;
    }

//    public boolean sendFaceToServer(String filePath) {
//
//        //preparing post params
//        Log.d(Statics.LOG, "Image Path: " + filePath);
//
//        ServiceHandler serviceClient = new ServiceHandler();
//
//        String json = serviceClient.makeServiceCall(BH_IMAGE_DIRECTORY, ServiceHandler.POST, new File(filePath));
//
//        Log.d(Statics.LOG, "Reponse after sending image" + json);
//
////        if (json != null) {
////            try {
////                JSONObject jsonObj = new JSONObject(json);
////                boolean error = jsonObj.getBoolean("error");
////                // checking for error node in json
////                if (!error) {
////                    // new category created successfully
////                    Log.e("Agent added", "> " + jsonObj.getString("output"));
////                } else {
////                    Log.e("Add Prediction Error: ",
////                            "> " + jsonObj.getString("output"));
////                }
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////                return false;
////            }
////
////        } else {
////            Log.e("JSON Data", "JSON data error!");
////        }
//
//        /* update the text on the current screen the user is seeing to tell them their user profile
//        was saved successfully. */
//        Messenger messenger = new Messenger();
//        messenger.sendSaveResults("Agent profile created successfully! You may proceed.");
//        return true;
//    }

    public boolean enrollFaceFromImage(String email, String fileName) {

        List<NameValuePair> params = new ArrayList<NameValuePair>(4);

        params.add(new BasicNameValuePair("image", fileName));
        params.add(new BasicNameValuePair("album", GALLERY_ID));
        params.add(new BasicNameValuePair("albumkey", ALBUM_KEY));
        params.add(new BasicNameValuePair("entryid", email));

        ServiceHandler serviceHandler = new ServiceHandler();
        String response = serviceHandler.makeServiceCall(LINKER_ALBUM_TRAIN, ServiceHandler.POST, params);
        Log.d(Statics.LOG, "Response: " + response);
        return true;
    }

    /**
     * Top level call to recognize a face with the api.
     *
     * @param filePath absolute file path of the image being used.
     * @return true if request went through
     */
    public boolean recognizeFaceFromImage(String filePath) {

        json_parameters = new JSONObject();

        try {
            json_parameters.put("files", new File(filePath));
            json_parameters.put("album", GALLERY_ID);
            json_parameters.put("albumkey", ALBUM_KEY);

            /* create our http post object */
            HttpPost httpPost = new HttpPost(RECOGNIZE_URL);
            httpPost.addHeader("Content-type", "application/json");
            httpPost.addHeader("X-Mashape-Key", API_KEY);

            /* use the httpPost object and get our reponse from the api */
            response = doPostRequest(httpPost);

            Log.d("SENT", "async http client sent request to recognize");
            Log.d("RESPONSE", "Response: " + response);

            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean listAllFaces() {


        //asyncHttpClient.addHeader("X-Mashape-Key", API_KEY);
        //asyncHttpClient.addHeader("Accept", "application/json");
        //asyncHttpClient.get(VIEW_ALBUM_URL, jsonHttpResponseHandler);

        return true;

    }

    public String doPostRequest(HttpPost httpPost) {
        String responseString = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();

        try {

            /* json_parameters are set in calling methods */
            if(json_parameters != null) {
                StringEntity se = new StringEntity(json_parameters.toString(), "utf-8");
                httpPost.setEntity(se);
            }

            HttpResponse response = null;

            response = httpclient.execute(httpPost);
            responseString = EntityUtils.toString(response
                    .getEntity(), "UTF-8");
            responseCode = response.getStatusLine().getStatusCode();
            Log.d("RESCODE", "" + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }
}
