package com.noahbutler.agentyou.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.noahbutler.agentyou.R;

/**
 * Created by Noah Butler on 4/1/2015.
 */
public class LoginFragment extends Fragment {

    EditText agentsEmailInput, agentsPassInput;
    Button agentRegisterBtn, agentLoginBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        /* TODO: create login check */
        agentsEmailInput = (EditText)rootView.findViewById(R.id.agents_email_input_login);
        agentsPassInput  = (EditText)rootView.findViewById(R.id.agents_pass_input_login);

        agentLoginBtn    = (Button)rootView.findViewById(R.id.agents_login_btn);
        agentLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.main_content_area, new DashBoardFragment()).commit();
            }
        });

        agentRegisterBtn = (Button)rootView.findViewById(R.id.agents_register_btn);
        agentRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.main_content_area, new RegisterFragment()).commit();
            }
        });

        return rootView;
    }
}
