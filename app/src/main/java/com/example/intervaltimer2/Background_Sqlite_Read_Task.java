package com.example.intervaltimer2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Locale;

public class Background_Sqlite_Read_Task extends AsyncTask<String,Void,ProfileAdapter> {
    private RecyclerView recyclerView;
    private Context context;
    private ProfileAdapter backgroundProfileAdapter;
    private String Id;
    private String method;


    private String dataretreived;
    private int numOfSets;
    private int dataId;
    private long warmUpMillis;
    private long lowIntensityMillis;
    private long highIntensityMillis;
    private String profileName;
    private long dataretreivedLong;
    private String timeLeftFormatted;
    private int minutes;
    private int seconds;
    private Cursor res;
    private long warmupLong;
    private long lowintLong;
    private long highintLong;
    private int sets;

    public Background_Sqlite_Read_Task(String Id, Context context) {
        this.context = context;
        this.Id = Id;
    }

    public Background_Sqlite_Read_Task(String Id, RecyclerView recyclerView, Context context) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.Id = Id;
    }

    public Background_Sqlite_Read_Task(ProfileAdapter adapter,RecyclerView recyclerView, Context context) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.backgroundProfileAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ProfileAdapter doInBackground(String... strings) {
        method = strings[0];
        ProfileDBHelper dbHelperBackground = new ProfileDBHelper(context);

        switch(method) {
            case "getAllData" :
                ProfileDBHelper dbHelperBackground1 = new ProfileDBHelper(context);
                backgroundProfileAdapter = new ProfileAdapter(context, dbHelperBackground1.getAllData());
                break;
            case "getSpecificData" :
                res  = dbHelperBackground.getSpecificData(Id);
                if(res.getCount() == 0) {
                    //Toast.makeText(context,"getSpecificData() - Asynctask - Data not received", Toast.LENGTH_SHORT).show();
                    return null;
                } else {
                    getSpecificDataTask(res);
                }
                break;
            case "getSpecificDataSwipeRight" :
            case "getSpecificDataSwipeLeft" :
                res  = dbHelperBackground.getSpecificData(Id);
                if(res.getCount() == 0) {
                    Toast.makeText(context,"getSpecificData() - Asynctask - Data not received", Toast.LENGTH_SHORT).show();
                    return null;
                }
                break;
            case "deleteData" :
                dbHelperBackground.deleteData(Id);
                break;
        }
        dbHelperBackground.close();
        return backgroundProfileAdapter;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ProfileAdapter adapter) {
        switch(method) {
            case "getAllData" :
                recyclerView.setAdapter(backgroundProfileAdapter);
                break;
            case "getSpecificData" :
                openIntervalTimerProfile();
                break;
            case "getSpecificDataSwipeRight" :
                showDetailsOfProfile(res);
                break;
            case "getSpecificDataSwipeLeft" :
                openProfileForUpdate(res);
                break;
            case "deleteData" :
                break;
        }
    }







    public void getSpecificDataTask(Cursor res) {
        while(res.moveToNext()) {
            dataretreived = res.getString(0);
            dataId = Integer.parseInt(dataretreived);

            dataretreived = res.getString(1);

            dataretreived = res.getString(2);
            warmUpMillis = Long.parseLong(dataretreived);

            dataretreived = res.getString(3);
            lowIntensityMillis = Long.parseLong(dataretreived);

            dataretreived = res.getString(4);
            highIntensityMillis = Long.parseLong(dataretreived);

            dataretreived = res.getString(5);
            numOfSets = Integer.parseInt(dataretreived);
        }
    }

    public void openNewProfileCreate(String Id, String name,long warmupTime, long lowIntTime,long HighIntTime,int sets, int saveUpdate, int quickAdd) {
        Intent intent2 = new Intent(context, IntervalTmerCustom.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("profileId", Id);
        intent2.putExtra("profileName",name);
        intent2.putExtra("warmUpTime",warmupTime);
        intent2.putExtra("lowIntensityTime", lowIntTime);
        intent2.putExtra("highIntensity", HighIntTime);
        intent2.putExtra("sets", sets);
        intent2.putExtra("saveUpdate", saveUpdate);
        intent2.putExtra("quickAdd", quickAdd);
        context.startActivity(intent2);
    }

    public void openIntervalTimerProfile() {
        Intent intent = new Intent(context, IntervalTimer1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("warmUpTimeInMillis",warmUpMillis);
        intent.putExtra("lowIntensityTimeInMillis", lowIntensityMillis);
        intent.putExtra("highIntensityInMillis", highIntensityMillis);
        intent.putExtra("numOfSets", numOfSets);
        context.startActivity(intent);
    }

    public void showDetailsOfProfile(Cursor res) {
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()) {
            profileName = res.getString(1);

            dataretreived = res.getString(2);
            dataretreivedLong = Long.parseLong(dataretreived);
            minutes = (int) ((dataretreivedLong)/1000) /60;
            seconds = (int) ((dataretreivedLong)/1000) %60;
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            buffer.append("Warmup Time - " + timeLeftFormatted +"\n");


            dataretreived = res.getString(3);
            dataretreivedLong = Long.parseLong(dataretreived);
            minutes = (int) ((dataretreivedLong)/1000) /60;
            seconds = (int) ((dataretreivedLong)/1000) %60;
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            buffer.append("Low Int. Time - " + timeLeftFormatted +"\n");


            dataretreived = res.getString(4);
            dataretreivedLong = Long.parseLong(dataretreived);
            minutes = (int) ((dataretreivedLong)/1000) /60;
            seconds = (int) ((dataretreivedLong)/1000) %60;
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            buffer.append("High Int. Time - " + timeLeftFormatted +"\n");


            buffer.append("Sets - " + res.getString(5) +"\n");
        }
        AlertDialog.Builder builder;
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            builder = new AlertDialog.Builder(context, R.style.MyDialogThemeDark);
        } else {
            builder = new AlertDialog.Builder(context, R.style.MyDialogThemeLight);
        }
        builder.setCancelable(true);
        builder.setTitle("Profile - "+profileName);
        builder.setMessage(buffer.toString());
        builder.show();
    }

    void openProfileForUpdate(Cursor res) {
        while(res.moveToNext()) {
            profileName = res.getString(1);

            dataretreived = res.getString(2);
            warmupLong = Long.parseLong(dataretreived);

            dataretreived = res.getString(3);
            lowintLong = Long.parseLong(dataretreived);

            dataretreived = res.getString(4);
            highintLong = Long.parseLong(dataretreived);

            dataretreived = res.getString(5);
            sets = Integer.parseInt(dataretreived);
        }
        openNewProfileCreate(Id,profileName,warmupLong,lowintLong,highintLong,sets, 1, 0);

    }
}
