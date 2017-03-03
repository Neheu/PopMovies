package movies.proj.com.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import movies.proj.com.popularmovies.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(SplashScreenActivity.this, PopularMoviesActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, TIME_OUT);
    }

}
