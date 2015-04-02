package com.noahbutler.agentyou.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.noahbutler.agentyou.MainActivity;
import com.noahbutler.agentyou.R;
import com.noahbutler.agentyou.data.Statics;
import com.noahbutler.agentyou.utilities.db.DatabaseContract;
import com.noahbutler.agentyou.utilities.photo.AgentHashCreator;
import com.noahbutler.agentyou.utilities.threads.Messenger;

/**
 * Created by Noah Butler on 3/27/2015.
 */
public class StorageFragment extends Fragment{

    private String fileLocation, agent, email, pass;

    private MainActivity mainActivity;

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
        Bundle bundle = getArguments();
        fileLocation  = bundle.getString("location");
        agent         = bundle.getString("agent");
        email         = bundle.getString("email");
        pass          = bundle.getString("pass");

        AgentHashCreator agentHashCreator = new AgentHashCreator(fileLocation);

        if(agentHashCreator.createHashValueFromImage()) {

            String results = agentHashCreator.getResults();
            Log.d("RES", "HASH CODE: " + results);

            /* send info to the database */
            DatabaseContract databaseContract = new DatabaseContract();
            databaseContract.execute("sendNewUser", agent, results);

        }

        return rootView;

    }

}
