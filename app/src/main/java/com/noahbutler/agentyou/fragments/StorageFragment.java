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

import com.noahbutler.agentyou.R;
import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.db.DatabaseContract;
import com.noahbutler.agentyou.utilities.db.UploadAgentPicture;
import com.noahbutler.agentyou.utilities.photo.FaceLinker;
import com.noahbutler.agentyou.utilities.photo.LuxandFace;
import com.noahbutler.agentyou.utilities.threads.Messenger;

import java.util.HashMap;

/**
 * Created by Noah Butler on 3/27/2015.
 *
 * After the agent takes their photo
 */
public class StorageFragment extends Fragment{

    private String fileLocation, agent, email, pass;
    private TextView resultsDisplayText;
    private Button backToDashboardButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.waiting_fragment, container, false);

        resultsDisplayText = (TextView)rootView.findViewById(R.id.result_display_text);
        resultsDisplayText.setText("Sending user info to database, please wait...");

        backToDashboardButton = (Button)rootView.findViewById(R.id.back_to_dashboard_button);
        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.main_content_area, new DashBoardFragment()).commit();
            }
        });

        Statics.handler = new Handler() {

            @Override
            public void handleMessage(Message message) {
                resultsDisplayText.setText(message.getData().getString(Messenger.keys[1]));
            }

        };

        /* grab the string of the file that we want to turn into a hash value */
        final Bundle bundle = getArguments();
        fileLocation  = bundle.getString("location");
        agent         = bundle.getString("agent");
        email         = bundle.getString("email");
        pass          = bundle.getString("pass");

        UploadAgentPicture uploadAgentPicture = new UploadAgentPicture();
        uploadAgentPicture.setAgentEmail(email);
        /* TODO: currently not running, uses mashape api*/
        //uploadAgentPicture.execute(fileLocation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LuxandFace luxandFace = new LuxandFace();
                HashMap<String, String> faceData;

                /* grab the face from the image and then prep it for the database */
                faceData = luxandFace.prepImageForDB(fileLocation);

                /* send face data to the database under the agent's email */
                DatabaseContract databaseContract = new DatabaseContract();
                databaseContract.execute("sendNewFaceData", email, faceData.get("data"), faceData.get("bufferLength"));
            }
        }).start();

        /* TODO: currently not running, no .start() at the end */
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* send info to the database */
                DatabaseContract databaseContract = new DatabaseContract();
                databaseContract.execute("sendNewUser", agent, email, pass);
            }
        });

        return rootView;

    }

}
