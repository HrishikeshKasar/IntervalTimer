package com.example.intervaltimer2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class Background_Sqlite_Write_Task extends AsyncTask<String,Void,String>  {
    Context ctx;
    Background_Sqlite_Write_Task(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        String method = strings[0];
        String return_string = "Saved Profile";
        ProfileDBHelper dbHelperBackground = new ProfileDBHelper(ctx);

        if(method.equals("insertData")) {
            dbHelperBackground.insertData(strings[1], Long.parseLong(strings[2]), Long.parseLong(strings[3]), Long.parseLong(strings[4]), Integer.parseInt(strings[5]));
        } else if(method.equals("updateData")) {
            dbHelperBackground.updateData(strings[1],strings[2], Long.parseLong(strings[3]), Long.parseLong(strings[4]), Long.parseLong(strings[5]), Integer.parseInt(strings[6]));
        }

        dbHelperBackground.close();
        return return_string;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(ctx,s,Toast.LENGTH_SHORT).show();
    }
}
