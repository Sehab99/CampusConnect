package com.sehab.campusconnect.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehab.campusconnect.AddPostCampusActivity;
import com.sehab.campusconnect.R;
import com.sehab.campusconnect.adapters.CampusAdapter;
import com.sehab.campusconnect.models.Campus;

import java.util.ArrayList;

public class CampusFragment extends Fragment {
    FloatingActionButton addPost;
    TextView emptyFeed;

    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    CampusAdapter campusAdapter;
    ArrayList<Campus> campusFeed;
    RecyclerView contentRecyclerCampus;

    public CampusFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_campus, container, false);

        addPost = view.findViewById(R.id.add_post);
        emptyFeed = view.findViewById(R.id.empty_feed);
        contentRecyclerCampus = view.findViewById(R.id.content_recycler_campus);
        firebaseAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference("Post").child("Campus");
        mBase.keepSynced(true);
        contentRecyclerCampus.setHasFixedSize(true);
        contentRecyclerCampus.setLayoutManager(new LinearLayoutManager(getContext()));
        contentRecyclerCampus.setItemAnimator(new DefaultItemAnimator());

        mBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campusFeed = new ArrayList<>();
                if(snapshot.getChildrenCount() <= 0) {
                    emptyFeed.setVisibility(View.VISIBLE);
                } else {
                    emptyFeed.setVisibility(View.GONE);
                }

                for(DataSnapshot feedSnap : snapshot.getChildren()) {
                    //String postKey = feedSnap.getKey();

                    String post = feedSnap.child("post").getValue().toString();
                    String posterName =  feedSnap.child("posterName").getValue().toString();
                    String posterDept = feedSnap.child("posterDept").getValue().toString();
                    String date = feedSnap.child("date").getValue().toString();
                    String time = feedSnap.child("time").getValue().toString();
                    String posterUID= feedSnap.child("posterUID").getValue().toString();
                    campusFeed.add(new Campus(post, posterUID, posterName, posterDept, date,time));
                    int count=0;
                    count++;

                }
                campusAdapter = new CampusAdapter(getContext(),campusFeed);
                contentRecyclerCampus.setAdapter(campusAdapter);

                //Log.i("Populated List",campusFeed.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPostCampusActivity.class));
            }
        });
        return view;
    }
}
