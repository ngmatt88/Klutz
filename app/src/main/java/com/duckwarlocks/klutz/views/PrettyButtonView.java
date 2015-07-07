package com.duckwarlocks.klutz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duckwarlocks.klutz.R;

/**
 * Created by ngmat_000 on 7/7/2015.
 */
public class PrettyButtonView extends RelativeLayout {
    private TextView mSpeechBubble;
    private ImageView mBtnImage;

    public PrettyButtonView(Context context){
        super(context);
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prettybuton_compoundview, this);

        mBtnImage = (ImageView)findViewById(R.id.prettyBtnImg);
        mSpeechBubble = (TextView)findViewById(R.id.prettyBtnSpeech);
    }


    public PrettyButtonView(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prettybuton_compoundview, this);
        mBtnImage = (ImageView)findViewById(R.id.prettyBtnImg);
        mSpeechBubble = (TextView)findViewById(R.id.prettyBtnSpeech);
    }

    public void setmBtnImage(int resourceId){
        mBtnImage.setImageResource(resourceId);
    }

    public void setSpeechTxt(String speechTxt){
        mSpeechBubble.setBackgroundResource(R.drawable.speechbubble);
        mSpeechBubble.setText(speechTxt);
    }
}
