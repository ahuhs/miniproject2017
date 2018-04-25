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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.SlimViewHolder;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFrag extends Fragment {

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    ArrayList<MapRecycler> maps;
    SlimAdapter slimAdapter;
    Context applicationContext = MainActivity.getContextOfApplication();
    String unique_id = android.provider.Settings.Secure.getString(applicationContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    private FirebaseRecyclerAdapter<MapRecycler, MapFrag.MapViewHolder> firebaseRecyclerAdapter;


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

        mDatabase = FirebaseDatabase.getInstance().getReference().child("LocData").child(unique_id);
        recyclerView = (RecyclerView)view.findViewById(R.id.maprecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         maps = new ArrayList<>();

        /*mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    maps.clear();
                    for(DataSnapshot snap: dataSnapshot.getChildren()){
                        MapRecycler map = snap.getValue(MapRecycler.class);
                        maps.add(map);
                    }
                    slimAdapter.create().register(R.layout.map_card_list, new SlimInjector<MapRecycler>() {
                        @Override
                        public void onInject(MapRecycler model, IViewInjector injector) {
                            injector.text(R.id.map_name,model.getName());
                            ImageView map_image;
                            Switch active_switch;
                            ImageButton delete_map;

                            final String key = model.getKey();

                            map_image = (ImageView)injector.findViewById(R.id.map_img);
                            active_switch = (Switch)injector.findViewById(R.id.active_state);
                            delete_map = (ImageButton) injector.findViewById(R.id.map_delete);

                            Picasso.with(getContext()).load(model.getImage()).into(map_image);
                            map_image.setScaleType(ImageView.ScaleType.FIT_XY);
                            active_switch.setChecked(model.getActive());
                            active_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        mDatabase.child(key).child("active").setValue(true);

                                    }
                                    else {
                                        mDatabase.child(key).child("active").setValue(false);
                                    }
                                }
                            });
                            delete_map.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mDatabase.child(key).removeValue();
                                    slimAdapter.notifyDataSetChanged();
                                }
                            });

                        }
                    }) .enableDiff()
                            .updateData(maps)
                            .attachTo(recyclerView);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

    }

    @Override
    public void onStart() {
        super.onStart();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MapRecycler, MapFrag.MapViewHolder>(
                MapRecycler.class,
                R.layout.map_card_list,
                MapFrag.MapViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final MapFrag.MapViewHolder viewHolder, final MapRecycler model, final int position) {
                final String key = firebaseRecyclerAdapter.getRef(position).getKey();
                viewHolder.Name.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.map_image);
                viewHolder.map_image.setScaleType(ImageView.ScaleType.FIT_XY);
                viewHolder.active_switch.setChecked(model.getActive());
                mDatabase.child(key).child("key").setValue(key);
                viewHolder.active_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            mDatabase.child(key).child("active").setValue(true);
                        }
                        else {
                            mDatabase.child(key).child("active").setValue(false);
                        }
                    }
                });
                try{
                    viewHolder.delete_map.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase.child(key).removeValue();
                        }
                    });
                }catch (Exception e){

                }
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
    public static class MapViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView Name;
        ImageView map_image;
        Switch active_switch;
        ImageButton delete_map;

        public MapViewHolder(View itemView) {
            super(itemView);

            //mView = itemView;
            Name = (TextView)itemView.findViewById(R.id.map_name);
            map_image = (ImageView)itemView.findViewById(R.id.map_img);
            active_switch = (Switch)itemView.findViewById(R.id.active_state);
            delete_map = (ImageButton) itemView.findViewById(R.id.map_delete);
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
        public void setActive(Boolean active){
            Switch act_switch = (Switch)mView.findViewById(R.id.active_state);
            act_switch.setChecked(active);
        }

    }

}
