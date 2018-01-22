package com.a4dotsinc.profilo;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFrag extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;



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

        FirebaseRecyclerAdapter<TimeRecycler, TimeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TimeRecycler, TimeViewHolder>(
                TimeRecycler.class,
                R.layout.timer_card_list,
                TimeViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(TimeViewHolder viewHolder, TimeRecycler model, int position) {
                viewHolder.setStart(model.getHead());
                viewHolder.setStop(model.getDesc());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class TimeViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TimeViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setStart(String st){

            TextView start = (TextView)mView.findViewById(R.id.startTimer);
            Log.e(st, "setStart: ");
            start.setText(st);


        }
        public void setStop(String sto){

            TextView stop = (TextView)mView.findViewById(R.id.endTimer);
            Log.e(sto, "setStop: ");
            stop.setText(sto);


        }
    }

}
