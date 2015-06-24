package com.noahbutler.agentyou.utilities.threads;

import android.os.Bundle;
import android.os.Message;

import com.noahbutler.agentyou.data.Statics;

/**
 * Created by Noah Butler on 3/28/2015.
 */
public class Messenger {

    Message message;
    Bundle bundle;
    public static String[] keys = {"HammingResults", "SavedResults", "sendFaceData"};

    private void init() {
        message = new Message();
        bundle = new Bundle();
    }

    public void sendHammingResults(String results) {
        init();
        bundle.putString(keys[0], results);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void sendSaveResults(String msg) {
        init();
        bundle.putString(keys[1], msg);
        message.setData(bundle);
        Statics.handler.sendMessage(message);
    }

    public void sendFaceData(String msg) {
        init();
        bundle.putString(keys[2], msg);
        message.setData(bundle);
        HandlerPool.faceDataHandler.sendMessage(message);
    }


}
