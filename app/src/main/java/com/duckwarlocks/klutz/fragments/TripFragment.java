package com.duckwarlocks.klutz.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duckwarlocks.klutz.MainActivity;
import com.duckwarlocks.klutz.R;


public class TripFragment extends Fragment {

    private View rootView;
    private TextView mTextViewCoordinates;
    private Fragment fragment;

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

    public void goToTripLocations(){

        fragment = new SavedLocationsFragment();
        MainActivity.setmCurrentFragment(fragment);
        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trip, container, false);
        mTextViewCoordinates = (TextView) rootView.findViewById(R.id.currentCoordinatesTrip);
        goToTripLocations();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
