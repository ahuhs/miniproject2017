package com.a4dotsinc.profilo;


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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class WishlistFrag extends Fragment {

    RecyclerView recyclerView ;
    DatabaseReference mDatabase;


    public WishlistFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Phone");

        recyclerView = (RecyclerView)view.findViewById(R.id.numRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<WishRecycler, WishlistFrag.WishViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<WishRecycler, WishlistFrag.WishViewHolder>(
                WishRecycler.class,
                R.layout.num_list,
                WishlistFrag.WishViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(WishlistFrag.WishViewHolder viewHolder, WishRecycler model, int position) {
                viewHolder.number.setText(model.getNumber());
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class WishViewHolder extends RecyclerView.ViewHolder{

        TextView number;

        public WishViewHolder(View itemView) {
            super(itemView);

            number = (TextView)itemView.findViewById(R.id.txtnumber);
        }
    }
}
