package com.a4dotsinc.profilo;

import android.app.TimePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Timepic_Activity extends AppCompatActivity  implements TimePickerDialog.OnTimeSetListener{

    ImageButton back;
    TextView st, sto;
    FloatingActionButton sub;
    Firebase timeurl;
    DatabaseReference mDatabase;
    String ss;
    boolean hh=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepic_);
        back = (ImageButton)findViewById(R.id.back_btn);
        sub = (FloatingActionButton) findViewById(R.id.add_btn);
        st = (TextView) findViewById(R.id.add_start_id);
        sto = (TextView) findViewById(R.id.add_stop_id);
        Firebase.setAndroidContext(this);

        timeurl = new Firebase("https://profilo-190814.firebaseio.com/Timer/testuser/").push();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Timer").child("testuser");

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
                Toast.makeText(Timepic_Activity.this, "Time Added!!",Toast.LENGTH_SHORT).show();
                finish();
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
        TimeRecycler timeRecycler = new TimeRecycler(st.getText().toString(),sto.getText().toString(), true);
        mDatabase.push().setValue(timeRecycler);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        if (!hh){
            st.setText(i+" "+i1);
        }
        else {
            sto.setText(i+" "+i1);

        }
    }
}
