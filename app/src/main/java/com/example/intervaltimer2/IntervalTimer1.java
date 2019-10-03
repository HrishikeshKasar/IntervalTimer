package com.example.intervaltimer2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IntervalTimer1 extends AppCompatActivity implements  dialog_box.dialogBoxListener {

    private long WARMUP_TIME_IN_MILLIs = 0;
    private long LOW_INTENSITY_TIME_IN_MILLIs = 0;
    private long HIGH_INTENSITY_TIME_IN_MILLIs = 0;
    private int NUM_OF_SETS = 0;
    private static final int warmUpStage = 1;
    private static final int lowIntStage = 2;
    private static final int highIntStage = 3;
    private static final int finishedStage = -1;
    private static final int init = 0;
    private static final int statechange = 1;

    List<String> ExerciseProgressionArrayString = new ArrayList<>();
    private long ExerciseStageTimes[] = new long[4];
    private int currentStageCounter;
    private int AppOperationStage;
    private static final int AppStageInitial = 1;
    private static final int AppStageStarted = 2;
    private static final int AppStageCompleted = 3;
    private int numOfSetsVal = 0;
    private static final int paddingTimeWarmup = 0;
    private static final int paddingTime = 0;


    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private RelativeLayout background;
    private CountDownTimer mCountDownTimer;
    private TextView ESText;
    private TextView SetsText;
    private MediaPlayer mediaPlayer;


    private boolean mTimerRunning;
    private boolean mTimerExit;
    private long mTimeLeftInMillis ;
    private long mEndTime;
    private long START_TIME_IN_MILLIS;
    private int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_interval_timer1);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        background = findViewById(R.id.RL_background);
        ESText = findViewById(R.id.TxtView_ExStageName);
        SetsText = findViewById(R.id.TxtView_setNum);
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        WARMUP_TIME_IN_MILLIs = getIntent().getLongExtra("warmUpTimeInMillis", 0);
        WARMUP_TIME_IN_MILLIs = WARMUP_TIME_IN_MILLIs + paddingTimeWarmup;
        LOW_INTENSITY_TIME_IN_MILLIs = getIntent().getLongExtra("lowIntensityTimeInMillis", 0);
        LOW_INTENSITY_TIME_IN_MILLIs = LOW_INTENSITY_TIME_IN_MILLIs + paddingTime;
        HIGH_INTENSITY_TIME_IN_MILLIs = getIntent().getLongExtra("highIntensityInMillis", 0);
        HIGH_INTENSITY_TIME_IN_MILLIs = HIGH_INTENSITY_TIME_IN_MILLIs + paddingTime;
        NUM_OF_SETS = getIntent().getIntExtra("numOfSets", 1);
        fillExerciseProgression();
        initializeComplete();

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppOperationStage == AppStageInitial) {
                    AppOperationStage = AppStageStarted;
                }
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                resetTimer();
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetTimer();
        finish();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinishes) {
                mTimeLeftInMillis = millisUntilFinishes;
                updateCountDownText();
                if(mTimeLeftInMillis < 3000) {
                    mediaPlayer.start();
                }
            }

            @Override
            public void onFinish() {
                mTimerRunning= false;
                goToNextStage();
                updateButtons();
                updateUI();
                updateStageCounter(statechange);
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        if(mTimerRunning == true) {
            pauseTimer();
        }
        initializeComplete();
    }

    private void updateSets() {
        numOfSetsVal++;
        SetsText.setText("SETS : " + String.valueOf(numOfSetsVal) + "/" + String.valueOf(NUM_OF_SETS));
    }

    private void updateUI() {
        switch (Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter))) {
            case warmUpStage:
                ESText.setText("Warmup");
                SetsText.setText("SETS : " + String.valueOf(NUM_OF_SETS) );
                background.setBackground(ContextCompat.getDrawable(this,R.drawable.gradient_warmup));
                break;
            case lowIntStage:
                ESText.setText("low Intensity");
                background.setBackground(ContextCompat.getDrawable(this,R.drawable.gradient_low_int));
                updateSets();
                break;
            case highIntStage:
                ESText.setText("HighIntensity");
                background.setBackground(ContextCompat.getDrawable(this,R.drawable.gradient_high_int));
                if(LOW_INTENSITY_TIME_IN_MILLIs == paddingTime) {
                    updateSets();
                }
                break;
        }
    }

    private void updateButtons() {
        if(mTimerRunning) {
            mButtonStartPause.setText("Pause");
        } else {
            mButtonStartPause.setText("Start");

            if(mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateCountDownText() {
        int minutes = (int) ((mTimeLeftInMillis)/1000) /60;
        int seconds = (int) ((mTimeLeftInMillis)/1000) %60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }


    private void updateStageCounter(int state) {
        if(state == statechange) {
            if(Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter)) == -1) {
                currentStageCounter = 0;
            } else {
                currentStageCounter++;
            }
        } else if(state == init){
            currentStageCounter = 0;
        }
    }


    private void fillExerciseProgression() {
        if(WARMUP_TIME_IN_MILLIs > paddingTimeWarmup) {
            //first stage will be warm up
            ExerciseProgressionArrayString.add(String.valueOf(warmUpStage));

            if((LOW_INTENSITY_TIME_IN_MILLIs > paddingTime) && (HIGH_INTENSITY_TIME_IN_MILLIs > paddingTime)) {
                //all 3 stages are present
                for(i = 1; i<(2*NUM_OF_SETS); i=i+2) {
                    ExerciseProgressionArrayString.add(String.valueOf(lowIntStage));
                    ExerciseProgressionArrayString.add(String.valueOf(highIntStage));
                }
            } else if((LOW_INTENSITY_TIME_IN_MILLIs == paddingTime) && (HIGH_INTENSITY_TIME_IN_MILLIs > paddingTime)) {
                //no low intensity only high intensity
                for(i=1; i<NUM_OF_SETS+1;i++) {
                    ExerciseProgressionArrayString.add(String.valueOf(highIntStage));
                }
            } else if((LOW_INTENSITY_TIME_IN_MILLIs > paddingTime) && (HIGH_INTENSITY_TIME_IN_MILLIs == paddingTime)) {
                //no high intensity only low intensity
                for(i=1; i<NUM_OF_SETS+1;i++) {
                    ExerciseProgressionArrayString.add(String.valueOf(lowIntStage));
                }
            }
        } else {
            //no warm up time
            if((LOW_INTENSITY_TIME_IN_MILLIs > paddingTime) && (HIGH_INTENSITY_TIME_IN_MILLIs > paddingTime)) {
                //all 3 stages are present
                for(i = 0; i<(2*NUM_OF_SETS); i=i+2) {
                    ExerciseProgressionArrayString.add(String.valueOf(lowIntStage));
                    ExerciseProgressionArrayString.add(String.valueOf(highIntStage));
                }
            } else if((LOW_INTENSITY_TIME_IN_MILLIs == paddingTime) && (HIGH_INTENSITY_TIME_IN_MILLIs > paddingTime)) {
                //no low intensity only high intensity
                for(i=0; i<NUM_OF_SETS;i++) {
                    ExerciseProgressionArrayString.add(String.valueOf(highIntStage));
                }
            } else if((LOW_INTENSITY_TIME_IN_MILLIs > paddingTime) && (HIGH_INTENSITY_TIME_IN_MILLIs == paddingTime)) {
                //no high intensity only low intensity
                for(i=0; i<NUM_OF_SETS;i++) {
                    ExerciseProgressionArrayString.add(String.valueOf(lowIntStage));
                }
            }
        }

        ExerciseProgressionArrayString.add("-1");
        ExerciseStageTimes[warmUpStage] = WARMUP_TIME_IN_MILLIs;
        ExerciseStageTimes[lowIntStage] = LOW_INTENSITY_TIME_IN_MILLIs;
        ExerciseStageTimes[highIntStage] = HIGH_INTENSITY_TIME_IN_MILLIs;

    }

    private void initializeComplete() {
        updateStageCounter(init);
        numOfSetsVal = 0;
        if((Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter)) == lowIntStage) ||
                (Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter)) == highIntStage)) {
            START_TIME_IN_MILLIS = ExerciseStageTimes[Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter))];
        } else {
            START_TIME_IN_MILLIS = ExerciseStageTimes[Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter))];
        }
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateUI();
        updateCountDownText();
        updateButtons();
        updateStageCounter(statechange);
        AppOperationStage = AppStageInitial;
        mTimerExit = false;
    }

    private void goToNextStage() {
        if(!mTimerExit) {
            switch (Integer.parseInt(ExerciseProgressionArrayString.get(currentStageCounter))) {
                case warmUpStage:
                    mTimeLeftInMillis = WARMUP_TIME_IN_MILLIs;
                    startTimer();
                    break;
                case lowIntStage:
                    mTimeLeftInMillis = LOW_INTENSITY_TIME_IN_MILLIs;
                    startTimer();
                    break;
                case highIntStage:
                    mTimeLeftInMillis = HIGH_INTENSITY_TIME_IN_MILLIs;
                    startTimer();
                    break;
                case finishedStage:
                    if (AppOperationStage == AppStageStarted) {
                        AppOperationStage = AppStageCompleted;
                    }
                    mTimerExit = true;
                    openDialog();
                    break;
            }
        }
    }

    private void openDialog() {
        dialog_box dialogBox = new dialog_box();
        dialogBox.show(getSupportFragmentManager(), "Distance Covered");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning",mTimerRunning);
        outState.putLong("endTime",mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        updateCountDownText();
        updateButtons();

        if(mTimerRunning) {
            mEndTime = savedInstanceState.getLong("endTime");
            mEndTime = mEndTime - System.currentTimeMillis();
            startTimer();
        }
    }

    @Override
    public boolean isChangingConfigurations() {
        return super.isChangingConfigurations();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putInt("currentStageCounter", currentStageCounter);
        editor.putInt( "ExerciseProgressionArray_size", ExerciseProgressionArrayString.size());
        for(int x=0;x<ExerciseProgressionArrayString.size();x++) {
            editor.putString("ExerciseProgressionArray_" + x, ExerciseProgressionArrayString.get(x));
        }
        editor.apply();
        if(mTimerRunning) {
            mCountDownTimer.cancel();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mTimeLeftInMillis = prefs.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        currentStageCounter = prefs.getInt("currentStageCounter", 0);
        int size = prefs.getInt("ExerciseProgressionArray_size", 0);
        for (int x = 0; x < size; x++) {
            ExerciseProgressionArrayString.add(prefs.getString( "ExerciseProgressionArray_size" + x, null));
        }

        updateCountDownText();
        updateButtons();

        if(mTimerRunning) {
            mEndTime = prefs.getLong("endTime",0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if(mTimeLeftInMillis < 0) {
                //mTimeLeftInMillis = 0;
                //mTimerRunning = false;
                goToNextStage();
                updateButtons();
                updateUI();
                updateStageCounter(statechange);
            } else {
                startTimer();
            }
        }
        if((AppOperationStage == AppStageInitial) || (AppOperationStage == AppStageCompleted)) {
            resetTimer();
        }
    }

    @Override
    public void applyTexts(String distance) {
        Intent intent = new Intent(this,IntervalTimer_SummaryPage.class);
        intent.putExtra("warmUpTimeInMillis", WARMUP_TIME_IN_MILLIs - paddingTimeWarmup);
        intent.putExtra("lowIntensityTimeInMillis",LOW_INTENSITY_TIME_IN_MILLIs - paddingTime);
        intent.putExtra("highIntensityInMillis", HIGH_INTENSITY_TIME_IN_MILLIs - paddingTime);
        intent.putExtra("numOfSets", NUM_OF_SETS);
        intent.putExtra("Distance", distance);
        this.startActivity(intent);
    }
}
