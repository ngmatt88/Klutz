package com.duckwarlocks.klutz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duckwarlocks.klutz.R;

/**
 * Created by ngmat_000 on 7/7/2015.
 */
public class PrettyButtonView extends RelativeLayout {
    private Button mBtn;
    private ImageView mBtnImage;

    public PrettyButtonView(Context context){
        super(context);
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prettybuton_compoundview, this);

        mBtnImage = (ImageView)findViewById(R.id.prettyBtnImg);
        mBtn = (Button)findViewById(R.id.prettyBtn);
    }


    public PrettyButtonView(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prettybuton_compoundview, this);
        mBtnImage = (ImageView)findViewById(R.id.prettyBtnImg);
        mBtn = (Button)findViewById(R.id.prettyBtn);
    }

    public void setBtnOnClick(OnClickListener listen){
        mBtn.setOnClickListener(listen);
    }

    public void setmBtnImage(int resourceId){
        mBtnImage.setImageResource(resourceId);

//        scaleImage();
    }

    public void setSpeechTxt(String speechTxt){
//        mSpeechBubble.setBackgroundResource(R.drawable.speechbubble);
        mBtn.setText(speechTxt);
    }
    private void scaleImage()
    {
        // Get the ImageView and its bitmap
        ImageView view = (ImageView) findViewById(R.id.prettyBtnImg);
        Drawable drawing = view.getDrawable();
        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.i("Test", "original width = " + Integer.toString(width));
        Log.i("Test", "original height = " + Integer.toString(height));
        Log.i("Test", "bounding = " + Integer.toString(bounding));

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.i("Test", "xScale = " + Float.toString(xScale));
        Log.i("Test", "yScale = " + Float.toString(yScale));
        Log.i("Test", "scale = " + Float.toString(scale));

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

        Log.i("Test", "done");
    }

    private int dpToPx(int dp)
    {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
