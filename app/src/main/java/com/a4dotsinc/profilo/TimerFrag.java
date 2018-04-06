package com.a4dotsinc.profilo;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFrag extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<TimeRecycler, TimeViewHolder> firebaseRecyclerAdapter;
    public AlarmManager alarmManager;

    private String key;



    public TimerFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Timer").child("testuser");

        recyclerView = (RecyclerView)view.findViewById(R.id.timerecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TimeRecycler, TimeViewHolder>(
                TimeRecycler.class,
                R.layout.timer_card_list,
                TimeViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(TimeViewHolder viewHolder, TimeRecycler model, final int position) {
                key = firebaseRecyclerAdapter.getRef(position).getKey();//"-L8Xn0V-HmODxevEGcVy";
                viewHolder.start.setText(model.getStarttime());
                viewHolder.stop.setText(model.getEndtime());
                Log.d(model.getStarttime(), "start");
                Log.d(model.getEndtime(), "stop");

                viewHolder.deleteTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key = firebaseRecyclerAdapter.getRef(position).getKey();
                        mDatabase.child(key).removeValue();
                    }
                });
                viewHolder.active_switch.setChecked(model.getActive());
                if (model.getActive()){
                    viewHolder.active_text_header.setText("Active");
                    viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#00e9af"));
                }
                else{
                    viewHolder.active_text_header.setText("Not Active");
                    viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#00a1d2"));
                }
                viewHolder.active_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                        if (b){
                            key = firebaseRecyclerAdapter.getRef(position).getKey();
                            mDatabase.child(key).child("active").setValue(true);
                            mDatabase.child(key).child("position").setValue(position+1);
                            mDatabase.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Log.d(dataSnapshot.child("active").getValue().toString()+String.valueOf(b)+position, "Active");
                                        active(Integer.parseInt(dataSnapshot.child("st_hr").getValue().toString()),
                                                Integer.parseInt(dataSnapshot.child("st_min").getValue().toString()),
                                                Integer.parseInt(dataSnapshot.child("position").getValue().toString()+"0"));
                                        active(Integer.parseInt(dataSnapshot.child("sto_hr").getValue().toString()),
                                                Integer.parseInt(dataSnapshot.child("sto_min").getValue().toString()),
                                                Integer.parseInt(dataSnapshot.child("position").getValue().toString()+"1"));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            key = firebaseRecyclerAdapter.getRef(position).getKey();
                            Log.d(key+position, "key");
                            mDatabase.child(key).child("active").setValue(false);
                            deactivate(Integer.parseInt((position+1)+"0"));
                            deactivate(Integer.parseInt((position+1)+"1"));
                        }
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private void active(int hr, int min, int position){
        Intent i = new Intent(getActivity(), Timed_Changes.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), position, i, 0);
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    private void deactivate(int position){
        Intent i = new Intent(getActivity(), Timed_Changes.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), position, i, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
    }
    public static class TimeViewHolder extends RecyclerView.ViewHolder{

        TextView start, stop, active_text_header;
        ImageButton deleteTime;
        Switch active_switch;
        RelativeLayout relativeLayout;


        public TimeViewHolder(View itemView) {
            super(itemView);

            start = (TextView)itemView.findViewById(R.id.startTimer);
            stop = (TextView)itemView.findViewById(R.id.endTimer);
            deleteTime = (ImageButton)itemView.findViewById(R.id.deleteTime);
            active_switch = (Switch)itemView.findViewById(R.id.timer_active_switch);
            active_text_header = (TextView)itemView.findViewById(R.id.time_header_text);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.time_header);
        }

    }

}
