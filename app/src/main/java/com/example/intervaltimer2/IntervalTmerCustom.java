package com.example.intervaltimer2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class IntervalTmerCustom extends AppCompatActivity {

    private EditText editText_sets;
    private EditText editText_warmup_mins;
    private EditText editText_warmup_secs;
    private EditText editText_highintensity_mins;
    private EditText editText_highintensity_secs;
    private EditText editText_lowintensity_mins;
    private EditText editText_lowintensity_secs;
    private EditText editText_profile_name;
    private Button btn_ITCustom_Save;

    private int numOfSets;
    private long warmUpMillis;
    private long lowIntensityMillis;
    private long highIntensityMillis;
    private String profileName;
    private String profileId;
    private int minutes;
    private int seconds;
    private int saveUpdate;
    private int quickAdd;

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
        setContentView(R.layout.activity_interval_tmer_custom);

        editText_sets = (EditText) findViewById(R.id.editText_sets);
        editText_warmup_mins = (EditText) findViewById(R.id.editText_warmup_mins);
        editText_warmup_secs = (EditText) findViewById(R.id.editText_warmup_secs);
        editText_highintensity_mins = (EditText) findViewById(R.id.editText_highintensity_mins);
        editText_highintensity_secs = (EditText) findViewById(R.id.editText_highintensity_secs);
        editText_lowintensity_mins = (EditText) findViewById(R.id.editText_lowtensity_mins);
        editText_lowintensity_secs = (EditText) findViewById(R.id.editText_lowtensity_secs);
        btn_ITCustom_Save = (Button) findViewById(R.id.btn_ITCustom_Save);
        editText_profile_name = (EditText)findViewById(R.id.editText_profile_name);


        quickAdd = getIntent().getIntExtra("quickAdd", 0);
        profileId = getIntent().getStringExtra("profileId");

        profileName = getIntent().getStringExtra("profileName");
        editText_profile_name.setText(profileName);

        numOfSets = getIntent().getIntExtra("sets", 1);
        editText_sets.setText(Integer.toString(numOfSets));

        warmUpMillis = getIntent().getLongExtra("warmUpTime", 0);
        minutes = (int) ((warmUpMillis)/1000) /60;
        seconds = (int) ((warmUpMillis)/1000) %60;
        editText_warmup_mins.setText(Integer.toString(minutes));
        editText_warmup_secs.setText(Integer.toString(seconds));


        lowIntensityMillis = getIntent().getLongExtra("lowIntensityTime", 0);
        minutes = (int) ((lowIntensityMillis)/1000) /60;
        seconds = (int) ((lowIntensityMillis)/1000) %60;
        editText_lowintensity_mins.setText(Integer.toString(minutes));
        editText_lowintensity_secs.setText(Integer.toString(seconds));


        highIntensityMillis = getIntent().getLongExtra("highIntensity", 0);
        minutes = (int) ((highIntensityMillis)/1000) /60;
        seconds = (int) ((highIntensityMillis)/1000) %60;
        editText_highintensity_mins.setText(Integer.toString(minutes));
        editText_highintensity_secs.setText(Integer.toString(seconds));

        saveUpdate = getIntent().getIntExtra("saveUpdate", 0);
        if(quickAdd == 0) {
            if (saveUpdate == 0) {
                btn_ITCustom_Save.setText("SAVE");
            } else if (saveUpdate == 1) {
                btn_ITCustom_Save.setText("UPDATE");
            }
        } else {
            btn_ITCustom_Save.setText("GO");
        }

        btn_ITCustom_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText_sets.getText().toString().length() != 0) {
                        numOfSets = Integer.parseInt(editText_sets.getText().toString());
                        if(numOfSets == 0) {
                            numOfSets = 1;
                            editText_sets.setText(Integer.toString(numOfSets));
                        }
                    } else {
                        numOfSets = 1;
                    }
                    if(editText_warmup_secs.getText().toString().length() != 0) {
                        warmUpMillis = Long.parseLong(editText_warmup_secs.getText().toString());
                    } else {
                        warmUpMillis = 0;
                    }
                    if(editText_warmup_mins.getText().toString().length() != 0) {
                        warmUpMillis = warmUpMillis + (Long.parseLong(editText_warmup_mins.getText().toString()) * 60);
                    } else {
                    }
                    if(editText_lowintensity_secs.getText().toString().length() != 0) {
                        lowIntensityMillis = Long.parseLong(editText_lowintensity_secs.getText().toString());
                    } else {
                        lowIntensityMillis = 0;
                    }
                    if(editText_lowintensity_mins.getText().toString().length() != 0) {
                        lowIntensityMillis = lowIntensityMillis + (Long.parseLong(editText_lowintensity_mins.getText().toString()) * 60);
                    } else {
                    }
                    if(editText_highintensity_secs.getText().toString().length() != 0) {
                        highIntensityMillis = Long.parseLong(editText_highintensity_secs.getText().toString());
                    } else {
                        highIntensityMillis = 0;
                    }
                    if(editText_highintensity_mins.getText().toString().length() != 0) {
                        highIntensityMillis = highIntensityMillis + (Long.parseLong(editText_highintensity_mins.getText().toString()) * 60);
                    } else {
                    }

                    warmUpMillis = warmUpMillis * 1000;
                    lowIntensityMillis = lowIntensityMillis * 1000;
                    highIntensityMillis = highIntensityMillis * 1000;

                    if(quickAdd == 0) {
                        if (editText_profile_name.getText().toString().trim().length() == 0) {
                            Toast.makeText(IntervalTmerCustom.this, "Please Enter name of profile", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            profileName = editText_profile_name.getText().toString();
                        }
                    } else {
                        profileName = "Quick Timer";
                    }
                    if((warmUpMillis> 0) || (lowIntensityMillis > 0) || (highIntensityMillis > 0)) {
                        if(quickAdd == 0) {
                            if (saveUpdate == 0) {
                                saveProfile();
                                Toast.makeText(IntervalTmerCustom.this, "New Profile Saved", Toast.LENGTH_SHORT).show();
                            } else if (saveUpdate == 1) {
                                updateProfile();
                                Toast.makeText(IntervalTmerCustom.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        } else {
                            //Toast.makeText(getBaseContext(), "Quick", Toast.LENGTH_SHORT).show();
                            openIntervalTimerProfile();
                        }
                    } else {
                        Toast.makeText(IntervalTmerCustom.this, "Please Enter a value to proceed", Toast.LENGTH_SHORT).show();
                    }
            }
        });


    }


    public void saveProfile() {
        Background_Sqlite_Write_Task backgroundSqliteWriteTask = new Background_Sqlite_Write_Task(getBaseContext());
        backgroundSqliteWriteTask.execute("insertData",profileName,String.valueOf(warmUpMillis), String.valueOf(lowIntensityMillis), String.valueOf(highIntensityMillis),String.valueOf(numOfSets));
    }

    public void updateProfile() {
        Background_Sqlite_Write_Task backgroundSqliteWriteTask = new Background_Sqlite_Write_Task(getBaseContext());
        backgroundSqliteWriteTask.execute("updateData",profileId,profileName,String.valueOf(warmUpMillis), String.valueOf(lowIntensityMillis), String.valueOf(highIntensityMillis),String.valueOf(numOfSets));
    }

    public void openIntervalTimerProfile() {
        Intent intent = new Intent(getBaseContext(), IntervalTimer1.class);
        intent.putExtra("warmUpTimeInMillis",warmUpMillis);
        intent.putExtra("lowIntensityTimeInMillis", lowIntensityMillis);
        intent.putExtra("highIntensityInMillis", highIntensityMillis);
        intent.putExtra("numOfSets", numOfSets);
        startActivity(intent);
    }
}
