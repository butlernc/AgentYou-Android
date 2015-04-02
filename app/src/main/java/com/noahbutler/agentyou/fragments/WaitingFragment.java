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
import com.noahbutler.agentyou.utilities.photo.AgentHashCompare;
import com.noahbutler.agentyou.utilities.photo.AgentHashCreator;
import com.noahbutler.agentyou.utilities.threads.Messenger;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

        /* create an id to test against the database */
        AgentHashCreator agentHashCreator = new AgentHashCreator(fileLocation);
        if(agentHashCreator.createHashValueFromImage()) {// creating the id worked

            String results = agentHashCreator.getResults();
            Log.d("RES", "HASH CODE: " + results);

            /* Get agents in database to test against and then send these results over to be tested */
            DatabaseContract databaseContract = new DatabaseContract();
            databaseContract.execute("loadAgents", results);
        }

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().replace(R.id.main_content_area, new DashBoardFragment()).commit();
            }
        });

        return rootView;

    }

}
