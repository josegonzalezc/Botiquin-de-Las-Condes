package cl.uilabs.botiquindelascondes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cl.uilabs.botiquindelascondes.R;

/**
 * Created by jose on 20-11-17.
 */

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_NS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Handler mHandler = new Handler();
        Runnable mRunnble = new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.getIntent(SplashActivity.this));
            }
        };
        mHandler.postDelayed(mRunnble, SPLASH_TIME_NS);


    }
}
