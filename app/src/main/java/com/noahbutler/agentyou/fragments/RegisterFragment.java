package com.noahbutler.agentyou.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.noahbutler.agentyou.R;

/**
 * Created by Noah Butler on 3/19/2015.
 */
public class RegisterFragment extends Fragment {

    EditText agentsNameInput, agentsEmailInput, agentsPasswordInput, agentsRePasswordInput;
    TextView createIDDesc;
    Button createAgentHashButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        createAgentHashButton = (Button)rootView.findViewById(R.id.create_agent_hash_button);
        agentsEmailInput      = (EditText)rootView.findViewById(R.id.agents_email_input);
        agentsNameInput       = (EditText)rootView.findViewById(R.id.agents_name_input);
        agentsPasswordInput   = (EditText)rootView.findViewById(R.id.agents_pass_input);
        agentsRePasswordInput = (EditText)rootView.findViewById(R.id.agents_re_pass_input);

        //TODO: test passwords against themselves


        createAgentHashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!agentsNameInput.getText().toString().contentEquals("")) {//check for blank entry

                    /* bundle up username and send it off to the agent hash creating fragment */
                    Bundle bundle = new Bundle();
                    bundle.putString("agent", agentsNameInput.getText().toString());
                    bundle.putString("email", agentsEmailInput.getText().toString());
                    bundle.putString("pass", agentsPasswordInput.getText().toString());

                    /* create new fragment, send over bundle */
                    AgentHashCreatingFragment agentHashCreatingFragment = new AgentHashCreatingFragment();
                    agentHashCreatingFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.main_content_area, agentHashCreatingFragment).commit();

                }
            }
        });

        return rootView;
    }
}
