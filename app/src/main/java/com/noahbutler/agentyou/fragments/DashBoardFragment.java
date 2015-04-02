package com.noahbutler.agentyou.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.noahbutler.agentyou.R;

/**
 * Created by Noah Butler on 3/3/2015.
 */
public class DashBoardFragment extends Fragment {

    TextView welcomeText;
    Button goToTagFragment, goToRegisterFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        /* show the user's name and total score */
        welcomeText = (TextView)rootView.findViewById(R.id.welcome_text);


        goToTagFragment = (Button)rootView.findViewById(R.id.go_to_tag_fragment);

        goToTagFragment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.main_content_area, new IdentifyFragment()).commit();
            }
        });

        /* stand in register button */
        goToRegisterFragment = (Button)rootView.findViewById(R.id.go_to_register_fragment);

        goToRegisterFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.main_content_area, new RegisterFragment()).commit();
            }
        });

        return rootView;
    }
}
