package com.noahbutler.agentyou.utilities.photo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.kairos.Kairos;
import com.kairos.KairosListener;
import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.threads.Messenger;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**
 * Created by Noah Butler on 3/10/2015.
 *
 * This class will be used to create a pHash value from
 * a given picture that was taken by the agent.
 */
public class KairosLinker extends Thread {

    private static final String API_KEY = "b06b7213f08e71ad88e683f1092492a3";
    private static final String APP_ID = "21dd8967";
    private static final String GALLERY_ID = "AGENT_YOU";

    private Kairos kairos;
    private KairosListener kairosListener;
    private String rString;
    public KairosLinker(Context context) {

        /* setup our kairos link */
        kairos = new Kairos();
        kairos.setAuthentication(context, APP_ID, API_KEY);

        kairosListener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                // your code here!
                Messenger messenger = new Messenger();
                messenger.sendHammingResults(response);
                Log.d(Statics.LOG, response);
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }
        };
    }

    public boolean detectFaceFromImage(String filePath) {
        Bitmap image = BitmapFactory.decodeFile(filePath);
        try {
            /* grab face from image. */
            kairos.detect(image, null, null, kairosListener);
            return true;
        } catch (JSONException | UnsupportedEncodingException e) {
            return false;
        }
    }

    public boolean enrollFaceFromImage(String email, String filePath) {
        Bitmap image = BitmapFactory.decodeFile(filePath);
        try {
            kairos.enroll(image, email, GALLERY_ID, null, null, null, kairosListener);
            Log.d(Statics.LOG, "Running this method");
            return true;
        }catch (JSONException | UnsupportedEncodingException e) {
            return false;
        }
    }

    public boolean recognizeFaceFromImage(String filePath) {
        Bitmap image = BitmapFactory.decodeFile(filePath);
        try {
            kairos.recognize(image, GALLERY_ID, null, null, null, null, kairosListener);
            return true;
        } catch (JSONException | UnsupportedEncodingException e) {
            return false;
        }
    }

    public boolean listAllFaces() {
        try {
            kairos.listGalleries(kairosListener);
            return true;
        } catch (JSONException | UnsupportedEncodingException e) {
            return false;
        }
    }
    public String getRString() {
        return rString;
    }
}
