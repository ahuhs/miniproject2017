package com.a4dotsinc.profilo;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFrag extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;


    public MapFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("LocData").child("TestUser");
        recyclerView = (RecyclerView)view.findViewById(R.id.maprecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<MapRecycler, MapFrag.MapViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MapRecycler, MapFrag.MapViewHolder>(
                MapRecycler.class,
                R.layout.map_card_list,
                MapFrag.MapViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(MapFrag.MapViewHolder viewHolder, MapRecycler model, int position) {
              //  viewHolder.setName(model.getName());
               // viewHolder.setImage(getContext(), model.getImage());
                viewHolder.Name.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.map_image);
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class MapViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView Name;
        ImageView map_image;

        public MapViewHolder(View itemView) {
            super(itemView);

            //mView = itemView;
            Name = (TextView)itemView.findViewById(R.id.map_name);
            map_image = (ImageView)itemView.findViewById(R.id.map_img);

        }
        public void setName(String name){

            TextView Name = (TextView)mView.findViewById(R.id.map_name);
            Log.e(name, "setStart: ");
            Name.setText(name);
        }

        public void setImage(Context ctx, String image){
            ImageView map_image = (ImageView)mView.findViewById(R.id.map_img);
            Log.e(image, "ImgData: ");
            Picasso.with(ctx).load(image).into(map_image);
        }

    }

}
