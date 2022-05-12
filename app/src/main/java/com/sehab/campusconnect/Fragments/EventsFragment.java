package com.sehab.campusconnect.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.sehab.campusconnect.AddEventActivity;
import com.sehab.campusconnect.R;
import com.sehab.campusconnect.adapters.EventAdapter;
import com.sehab.campusconnect.models.Event;

import java.util.ArrayList;

public class EventsFragment extends Fragment {
    FloatingActionButton addPost;
    TextView emptyFeed;

    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    EventAdapter eventAdapter;
    ArrayList<Event> eventArrayList;
    RecyclerView recyclerViewEvents;

    public EventsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        addPost = view.findViewById(R.id.add_post);
        emptyFeed = view.findViewById(R.id.empty_feed);
        recyclerViewEvents = view.findViewById(R.id.content_recycler_event);
        firebaseAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference("Post").child("Event");

        mBase.keepSynced(true);
        recyclerViewEvents.setHasFixedSize(true);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEvents.setItemAnimator(new DefaultItemAnimator());

        mBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventArrayList = new ArrayList<>();
                if(snapshot.getChildrenCount() <= 0) {
                    emptyFeed.setVisibility(View.VISIBLE);
                } else {
                    emptyFeed.setVisibility(View.GONE);
                }

                for(DataSnapshot eventSnap : snapshot.getChildren()) {
                    String eventName = eventSnap.child("eventName").getValue().toString();
                    String eventDate = eventSnap.child("eventDate").getValue().toString();
                    String eventTime = eventSnap.child("eventTime").getValue().toString();
                    String eventDesc = eventSnap.child("eventDesc").getValue().toString();
                    String posterName = eventSnap.child("posterName").getValue().toString();
                    String posterDept = eventSnap.child("posterDept").getValue().toString();
                    String date = eventSnap.child("date").getValue().toString();
                    String time = eventSnap.child("time").getValue().toString();
                    String posterUID = eventSnap.child("posterUID").getValue().toString();

                    eventArrayList.add(new Event(eventName, eventDate, eventTime, eventDesc,
                            posterName, posterDept, date, time, posterUID));
                }

                eventAdapter = new EventAdapter(getContext(), eventArrayList);
                recyclerViewEvents.setAdapter(eventAdapter);
                //Log.i("Populated List",eventArrayList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddEventActivity.class));
            }
        });
        return view;
    }
}
