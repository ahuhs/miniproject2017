package com.a4dotsinc.profilo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ARAVIND on 12-02-2018.
 */

public class CallReceiver extends BroadcastReceiver{
    DatabaseReference mDatabase;



    @Override
    public void onReceive(final Context context, Intent intent) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Phone");

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            final String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            //Toast.makeText(context, number, Toast.LENGTH_SHORT).show();
            final WishRecycler wishRecycler = new WishRecycler(number);
            mDatabase.push().setValue(wishRecycler);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                         WishRecycler wishRecycler1 = snapshot.getValue(WishRecycler.class);
                         if (wishRecycler1.getNumber().equals(number)){
                             //AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                             //audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                             Toast.makeText(context, "Found the Number", Toast.LENGTH_SHORT).show();
                             break;
                         }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
