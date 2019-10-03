package com.example.intervaltimer2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Boolean themeState;
    private Button btn_InterTimerCustom;
    private Button btn_IntervalTimerQuick;
    public static RecyclerView recyclerView;
    private Toolbar toolbar;
    public static ProfileAdapter mainActivityAdapter;
    public static ProfileDBHelper dbHelperMain;
    public static Context mainActivitycontext;
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        themeState = prefs.getBoolean("DarkTheme", false);
        if(themeState == true) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.dark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);





        btn_InterTimerCustom = (Button) findViewById(R.id.btn_IntervalTimerCustom);
        btn_IntervalTimerQuick = (Button) findViewById(R.id.btn_IntervalTimerQuick);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Interval Timer");
        toolbar.setSubtitle("Home");
        if(themeState == true) {
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.black));
        }




        //hrishi -> getAllData
        recyclerView = findViewById(R.id.recView_profiles_main_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        dbHelperMain = new ProfileDBHelper(this);
//        mainActivityAdapter = new ProfileAdapter(this);//,dbHelperMain.getAllData());
//        recyclerView.setAdapter(mainActivityAdapter);
        mainActivitycontext = this;
        Background_Sqlite_Read_Task bgetall = new Background_Sqlite_Read_Task(mainActivityAdapter,recyclerView, mainActivitycontext);
        bgetall.execute("getAllData");
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //hrishi -> getAllData
                Background_Sqlite_Read_Task bgetall = new Background_Sqlite_Read_Task(mainActivityAdapter,recyclerView, mainActivitycontext);
                bgetall.execute("getAllData");
//                mainActivityAdapter.swapCursor(dbHelperMain.getAllData());

                if (direction == ItemTouchHelper.RIGHT) {
                    Background_Sqlite_Read_Task backgroundSqliteReadTask = new Background_Sqlite_Read_Task(viewHolder.itemView.getTag().toString(),mainActivitycontext);
                    backgroundSqliteReadTask.execute("getSpecificDataSwipeRight");
                } else if (direction == ItemTouchHelper.LEFT) {
                    Background_Sqlite_Read_Task backgroundSqliteReadTask = new Background_Sqlite_Read_Task(viewHolder.itemView.getTag().toString(),mainActivitycontext);
                    backgroundSqliteReadTask.execute("getSpecificDataSwipeLeft");
                }
            }
        }).attachToRecyclerView(recyclerView);





        btn_InterTimerCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewProfileCreate(null,null,0,0,0,0, 0 , 0);
            }
        });


        btn_IntervalTimerQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQuickProfile(null,null,0,0,0,0, 0, 1);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.theme_menu_main);
        if(themeState == true) {
            item.setChecked(true);
        } else {
            item.setChecked(false);
        }
            return true;
    }

    private void showAboutDetails() {
        AlertDialog.Builder builder;
        if(themeState == true) {
            builder = new AlertDialog.Builder(this, R.style.MyDialogThemeDark);
        } else {
            builder = new AlertDialog.Builder(this, R.style.MyDialogThemeLight);
        }
        builder.setCancelable(true);
        builder.setTitle("About");
        builder.setMessage("Developer - Hrishikesh Kasar :P");
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.theme_menu_main :
                if(item.isChecked()) {
                    item.setChecked(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    themeState = false;
                } else {
                    item.setChecked(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    themeState = true;
                }
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("DarkTheme", themeState);
                editor.apply();
                restartApp();
                break;
            case R.id.about_menu_main :
                showAboutDetails();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //hrishi -> getAllData
//        mainActivityAdapter = new ProfileAdapter(this, dbHelperMain.getAllData());
//        recyclerView.setAdapter(mainActivityAdapter);
        Background_Sqlite_Read_Task bgetall = new Background_Sqlite_Read_Task(mainActivityAdapter,recyclerView, getBaseContext());
        bgetall.execute("getAllData");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if((backPressedTime + 2000) > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(),"Press back again to Exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void openNewProfileCreate(String Id, String name,long warmupTime, long lowIntTime,long HighIntTime,int sets, int saveUpdate, int quickAdd) {
        Intent intent2 = new Intent(getBaseContext(), IntervalTmerCustom.class);

        intent2.putExtra("profileId", Id);
        intent2.putExtra("profileName",name);
        intent2.putExtra("warmUpTime",warmupTime);
        intent2.putExtra("lowIntensityTime", lowIntTime);
        intent2.putExtra("highIntensity", HighIntTime);
        intent2.putExtra("sets", sets);
        intent2.putExtra("saveUpdate", saveUpdate);
        intent2.putExtra("quickAdd", quickAdd);
        startActivity(intent2);
    }

    public void openQuickProfile(String Id, String name,long warmupTime, long lowIntTime,long HighIntTime,int sets, int saveUpdate, int quickAdd) {
        Intent intent2 = new Intent(getBaseContext(), IntervalTmerCustom.class);

        intent2.putExtra("profileId", Id);
        intent2.putExtra("profileName",name);
        intent2.putExtra("warmUpTime",warmupTime);
        intent2.putExtra("lowIntensityTime", lowIntTime);
        intent2.putExtra("highIntensity", HighIntTime);
        intent2.putExtra("sets", sets);
        intent2.putExtra("saveUpdate", saveUpdate);
        intent2.putExtra("quickAdd", quickAdd);
        startActivity(intent2);
    }

    public static void updateRecyclerView() {
        //hrishi -> getAllData
//        mainActivityAdapter.swapCursor(dbHelperMain.getAllData());
        Background_Sqlite_Read_Task bgetall = new Background_Sqlite_Read_Task(mainActivityAdapter,recyclerView, mainActivitycontext);
        bgetall.execute("getAllData");
    }
}
