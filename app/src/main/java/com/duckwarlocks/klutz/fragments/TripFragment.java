package com.duckwarlocks.klutz.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.duckwarlocks.klutz.R;

import org.w3c.dom.Text;

public class TripFragment extends Fragment {

    public View rootView;
    public TextView mTextViewCoordinates;


    public static TripFragment newInstance() {
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
        rootView = inflater.inflate(R.layout.fragment_trip, container, false);
        mTextViewCoordinates = (TextView) rootView.findViewById(R.id.currentCoordinatesTrip);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
