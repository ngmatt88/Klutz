package com.duckwarlocks.klutz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duckwarlocks.klutz.Exceptions.StopProcessingException;
import com.duckwarlocks.klutz.constants.CommonConstants;
import com.duckwarlocks.klutz.utilities.AlertDialogHelper;
import com.duckwarlocks.klutz.utilities.FileHelper;
import com.duckwarlocks.klutz.utilities.GpsCoordinatesHelper;
import com.duckwarlocks.klutz.vo.LocationVO;

import java.sql.SQLException;


/**
 */
public class MainFragment extends Fragment {
    private GpsCoordinatesHelper gps;
    private String mCityName;
    private double mLatitude;
    private double mLongitude;

    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.fragment_main,container,false);
    }



}
