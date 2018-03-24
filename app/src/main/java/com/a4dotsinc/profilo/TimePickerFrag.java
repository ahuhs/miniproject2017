package com.a4dotsinc.profilo;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by ARAVIND on 02-01-2018.
 */

public class TimePickerFrag extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int mill = c.get(Calendar.MILLISECOND);
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hr, min,false);
    }
}
