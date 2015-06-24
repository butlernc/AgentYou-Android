package com.noahbutler.agentyou.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.luxand.FSDK;
import com.noahbutler.agentyou.R;
import com.noahbutler.agentyou.data.DatabaseStream;
import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.db.DatabaseContract;
import com.noahbutler.agentyou.utilities.photo.LuxandFace;
import com.noahbutler.agentyou.utilities.threads.HandlerPool;
import com.noahbutler.agentyou.utilities.threads.Messenger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noah Butler on 3/17/2015.
 */
public class WaitingFragment extends Fragment {

    private String fileLocation;

    private TextView resultsDisplayText;
    private Button backToDashboardButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.waiting_fragment, container, false);
        resultsDisplayText = (TextView)rootView.findViewById(R.id.result_display_text);
        resultsDisplayText.setText("Waiting for results, please wait...");
        backToDashboardButton = (Button)rootView.findViewById(R.id.back_to_dashboard_button);

        Statics.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                resultsDisplayText.setText(msg.getData().getString(Messenger.keys[0]));
            }
        };

        /* grab the string of the file that we want to turn into a hash value */
        Bundle bundle = getArguments();
        fileLocation  = bundle.getString("location");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                FaceLinker facePlusLinker = new FaceLinker();
//                facePlusLinker.recognizeFaceFromImage(fileLocation);
//            }
//        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                FaceLinker facePlusLinker = new FaceLinker();
//                facePlusLinker.listAllFaces();
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                /* grab all face data from database */
                DatabaseContract databaseContract = new DatabaseContract();
                databaseContract.execute("getFaceData");

                /* create face data object from picture taken */
                LuxandFace luxandFace = new LuxandFace();
                FSDK.FSDK_FaceTemplate currentTemplate = luxandFace.getFaceTemplateFromImage(fileLocation);

                /* create FSDK_Feature object for each row in the database */
                HashMap<String, ArrayList<String>> faceDataMap = DatabaseStream.EmailFaceData;
                HashMap<String, FSDK.FSDK_FaceTemplate> faceFeatureMap = new HashMap<>();
                ArrayList<String> emailKeys = DatabaseStream.emailKeys;

                for(int i = 0; i < emailKeys.size(); i++) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("data", faceDataMap.get(emailKeys.get(i)).get(0));
                    data.put("bufferLength", faceDataMap.get(emailKeys.get(i)).get(1));

                    FSDK.FSDK_FaceTemplate faceTemplate = luxandFace.prepImageDataFromDB(data);
                    faceFeatureMap.put(emailKeys.get(i), faceTemplate);
                }

                /* check for matches */
                int[] results = new int[emailKeys.size()];
                for(int i = 0; i < emailKeys.size(); i++) {
                    results[i] = luxandFace.match(faceFeatureMap.get(emailKeys.get(i)), currentTemplate);
                    Log.d(Statics.LOG, "results[" + i + "]: " + results[i] + ", Email: " + emailKeys.get(i));
                }
            }
        }).start();

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.main_content_area, new DashBoardFragment()).commit();
            }
        });

        return rootView;

    }

}
