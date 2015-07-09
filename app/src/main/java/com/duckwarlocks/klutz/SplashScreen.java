package com.duckwarlocks.klutz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.duckwarlocks.klutz.widgets.GifMovieView;

/**
 * Created by ngmat_000 on 7/8/2015.
 */
public class SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        GifMovieView gifView = (GifMovieView)findViewById(R.id.splashGif);
        gifView.setMovieResource(R.drawable.water_splash_screen_company_test);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1900);
                }catch(InterruptedException e){
                    Log.e(getClass().getName(),e.toString());
                    e.printStackTrace();
                    System.exit(1);
                }finally {
                    Intent intent = new Intent("com.duckwarlocks.klutz.MainActivity");
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        finish();
    }
}
