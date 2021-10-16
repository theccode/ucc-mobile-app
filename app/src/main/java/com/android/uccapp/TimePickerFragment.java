package com.android.uccapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {
    private TimePicker mTimePicker;
    public static final String EXTRA_TIME = "com.android.uccapp.time";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_time_picker, null);
        mTimePicker = (TimePicker) view.findViewById(R.id.dialogueStartTimePicker);

        return new AlertDialog.Builder(getContext() )
                .setView(view)
                .setTitle("Time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        String mode = hour >= 12 ? "PM":"AM";
                        String time  = hour + ":" + minute + ""+mode;
                        sendResult(Activity.RESULT_OK, time);
                    }
                })
                .create();
    }
    public  void sendResult(int resultCode, String time){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
