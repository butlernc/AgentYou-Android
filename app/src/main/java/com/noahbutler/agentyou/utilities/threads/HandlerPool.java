package com.noahbutler.agentyou.utilities.threads;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.noahbutler.agentyou.data.Statics;

/**
 * Created by Noah Butler on 5/12/2015.
 */
public class HandlerPool {

    public static Handler faceDataHandler;

    public static class FaceDataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(Statics.LOG, msg.getData().getString(Messenger.keys[2]));
        }
    }

    public static class TextUpdateHandler extends Handler {

        /* text view that is updated by this handler */
        public static TextView textView;

        @Override
        public void handleMessage(Message msg) {
            textView.setText(msg.getData().getString(Messenger.keys[0]));
        }
    }
}
