package com.example.intervaltimer2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;

public class IntervalTimer_SummaryPage extends AppCompatActivity {

    private long WARMUP_TIME_IN_MILLIs = 0;
    private long LOW_INTENSITY_TIME_IN_MILLIs = 0;
    private long HIGH_INTENSITY_TIME_IN_MILLIs = 0;
    private int NUM_OF_SETS = 0;
    private int minutes;
    private int seconds;
    private String distance;

    private TextView txtView_warmup_sessions;
    private TextView txtView_lowIntensity_sessions;
    private TextView txtView_highIntensity_sessions;
    private TextView txtView_warmup_time;
    private TextView txtView_lowIntensity_time;
    private TextView txtView_highIntensity_time;
    private TextView txtView_distance;
    private String display_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_interval_timer__summary_page);

        WARMUP_TIME_IN_MILLIs = getIntent().getLongExtra("warmUpTimeInMillis", 0);
        LOW_INTENSITY_TIME_IN_MILLIs = getIntent().getLongExtra("lowIntensityTimeInMillis", 0);
        HIGH_INTENSITY_TIME_IN_MILLIs = getIntent().getLongExtra("highIntensityInMillis", 0);
        NUM_OF_SETS = getIntent().getIntExtra("numOfSets", 0);
        NUM_OF_SETS = NUM_OF_SETS / 2;
        distance = getIntent().getStringExtra("Distance");

        txtView_warmup_sessions = findViewById(R.id.textView_warmup_sessions);
        txtView_lowIntensity_sessions = findViewById(R.id.textView_lowIntensity_sessions);
        txtView_highIntensity_sessions = findViewById(R.id.textView_highIntensity_sessions);
        txtView_warmup_time = findViewById(R.id.textView_warmup_time);
        txtView_lowIntensity_time = findViewById(R.id.textView_lowIntensity_time);
        txtView_highIntensity_time = findViewById(R.id.textView_highIntensity_time);
        txtView_distance = findViewById(R.id.textView_Distance);


        display_string = "Sessions = " + 1;
        txtView_warmup_sessions.setText(display_string);

        display_string = "Sessions = " + NUM_OF_SETS;
        txtView_lowIntensity_sessions.setText(display_string);

        display_string = "Sessions = " + NUM_OF_SETS;
        txtView_highIntensity_sessions.setText(display_string);



        minutes = (int) (WARMUP_TIME_IN_MILLIs/1000) /60;
        seconds = (int) (WARMUP_TIME_IN_MILLIs/1000) %60;
        display_string = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        display_string = "Total Time = " + display_string;
        txtView_warmup_time.setText(display_string);

        minutes = (int) ((LOW_INTENSITY_TIME_IN_MILLIs * NUM_OF_SETS)/1000) /60;
        seconds = (int) ((LOW_INTENSITY_TIME_IN_MILLIs * NUM_OF_SETS)/1000) %60;
        display_string = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        display_string = "Total Time = " + display_string;
        txtView_lowIntensity_time.setText(display_string);

        minutes = (int) ((HIGH_INTENSITY_TIME_IN_MILLIs * NUM_OF_SETS)/1000) /60;
        seconds = (int) ((HIGH_INTENSITY_TIME_IN_MILLIs * NUM_OF_SETS)/1000) %60;
        display_string = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        display_string = "Total Time = " + display_string;
        txtView_highIntensity_time.setText(display_string);

        display_string = distance + " km";
        txtView_distance.setText(display_string);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
