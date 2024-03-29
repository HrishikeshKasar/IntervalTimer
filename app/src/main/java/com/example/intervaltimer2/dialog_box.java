package com.example.intervaltimer2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class dialog_box extends AppCompatDialogFragment {
    private EditText editText_distance;
    private dialogBoxListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Distance covered").setNegativeButton("cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String distanceCovered = "0";
                listener.applyTexts(distanceCovered);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String distanceCovered = editText_distance.getText().toString();
                listener.applyTexts(distanceCovered);
            }
        });

        editText_distance = view.findViewById(R.id.editText_distance);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.applyTexts("0");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (dialogBoxListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement dialogListener");
        }

    }


    public interface dialogBoxListener {
        void applyTexts(String distance);
    }
}
