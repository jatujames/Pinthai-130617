package com.pinthai.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinthai.R;

/**
 * Created by WE on 3/8/2017.
 */

public class TutorialFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial,container,false);
        return rootView;
    }
}
