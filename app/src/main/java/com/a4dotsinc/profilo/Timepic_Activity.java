package com.a4dotsinc.profilo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Timepic_Activity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{

    ImageButton back;
    TextView st, sto;
    FloatingActionButton sub;
    Firebase timeurl;
    private int st_hr, st_min, sto_hr, sto_min;
    DatabaseReference mDatabase;
    long st_milli, sto_milli;
    boolean hh=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepic_);
        back = (ImageButton)findViewById(R.id.back);
        sub = (FloatingActionButton) findViewById(R.id.add_btn);
        st = (TextView) findViewById(R.id.add_start_id);
        sto = (TextView) findViewById(R.id.add_stop_id);
        Firebase.setAndroidContext(this);
        String unique_id = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Timer").child(unique_id);

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker = new TimePickerFrag();
                timepicker.show(getSupportFragmentManager(), "time picker");
                hh=false;
            }
        });

        sto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker = new TimePickerFrag();
                timepicker.show(getSupportFragmentManager(), "time picker");
                hh=true;
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtobase();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void addtobase() {
        String k = mDatabase.push().getKey();
        boolean digitsOnly = TextUtils.isDigitsOnly(String.valueOf(st_hr));
            TimeRecycler timeRecycler = new TimeRecycler(st.getText().toString(), sto.getText().toString(), st_hr, st_min, sto_hr, sto_min, false, k);
            mDatabase.child(k).setValue(timeRecycler);
            Alerter.create(Timepic_Activity.this)
                    .setTitle("Time Added!")
                    .setBackgroundColorRes(R.color.colorAccent)
                    .setIcon(R.drawable.ic_check_black_24dp)
                    .setDuration(2000)
                    .setOnHideListener(new OnHideAlertListener() {
                        @Override
                        public void onHide() {
                            finish();
                        }
                    })
                    .show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        long milliseconds = TimeUnit.SECONDS.toMillis(TimeUnit.HOURS.toSeconds(i) + TimeUnit.MINUTES.toSeconds(i1));
//      Time in Milliseconds !!!
      /*  Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        long sinceMid = (milliseconds+ offset) % (24 * 60 * 60 * 1000);
        Toast.makeText(this, String.valueOf(milliseconds), Toast.LENGTH_SHORT).show();*/

        if (!hh){
            if ((1<=i)&&(i<=11)){
                st.setText(i+":"+i1+":AM");
            }
            if (i==0){
                st.setText("12"+":"+i1+":AM");
            }
            if ((12<=i)&&(i<=23)){
                st.setText(i-12+":"+i1+":PM");
            }
            st_milli = milliseconds;
            st_hr = i; st_min = i1;
        }
        else {
            if ((1<=i)&&(i<=11)){
                sto.setText(i+":"+i1+":AM");
            }
            if (i==0){
                sto.setText("12"+":"+i1+":AM");
            }
            if ((12<=i)&&(i<=23)){
                sto.setText(i-12+":"+i1+":PM");
            }
            sto_milli = milliseconds;
            sto_hr = i; sto_min = i1;
        }
    }
}
