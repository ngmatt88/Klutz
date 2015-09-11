package com.duckwarlocks.klutz.events;

import android.view.View;

import com.hartsolution.bedrock.BaseEvent;

/**
 * Created by matt on 9/11/15.
 */
public class GrabGpsEvent extends BaseEvent {
    View view;

    public GrabGpsEvent(View v){
        this.view = v;
    }

    public View getView(){
        return view;
    }
}
