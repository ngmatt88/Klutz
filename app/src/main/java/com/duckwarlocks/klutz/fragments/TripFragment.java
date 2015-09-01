package com.duckwarlocks.klutz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duckwarlocks.klutz.R;

public class TripFragment extends Fragment {



    public static TripFragment newInstance(String param1, String param2) {
        TripFragment fragment = new TripFragment();
        return fragment;
    }

    public TripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
