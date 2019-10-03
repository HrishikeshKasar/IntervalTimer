package com.example.intervaltimer2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private RecyclerView recyclerView;



    public ProfileAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public ProfileAdapter(Context context) {
        mContext = context;
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textView_profile_item_name);
        }
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.profile_item, viewGroup, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileViewHolder profileViewHolder, final int i) {
        if(!mCursor.moveToPosition(i)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(ProfileContract.ProfileEntry.COLUMN_NAME));
        long id = mCursor.getLong(mCursor.getColumnIndex(ProfileContract.ProfileEntry._ID));

        profileViewHolder.nameText.setText(name);
        profileViewHolder.itemView.setTag(id);

        profileViewHolder.nameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Background_Sqlite_Read_Task backgroundSqliteReadTask = new Background_Sqlite_Read_Task(profileViewHolder.itemView.getTag().toString(),mContext);
                backgroundSqliteReadTask.execute("getSpecificData");
            }
        });



        profileViewHolder.nameText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setMessage("Do you want to Delete Profile?")
//                        .setCancelable(false)
//                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
                                Background_Sqlite_Read_Task backgroundSqliteReadTask = new Background_Sqlite_Read_Task(profileViewHolder.itemView.getTag().toString(),mContext);
                                backgroundSqliteReadTask.execute("deleteData");
                                MainActivity.updateRecyclerView();
//                            }
//                        })
//                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if(mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
