package com.sehab.campusconnect.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.sehab.campusconnect.AddPostHostelActivity;
import com.sehab.campusconnect.R;
import com.sehab.campusconnect.adapters.HostelAdapter;
import com.sehab.campusconnect.models.Hostel;

import java.util.ArrayList;

public class HostelFragment extends Fragment {
    FloatingActionButton addPost;
    TextView emptyFeed;

    FirebaseAuth firebaseAuth;
    DatabaseReference mBase, userRef;

    HostelAdapter hostelAdapter;
    ArrayList<Hostel> hostelFeed;
    RecyclerView contentRecyclerHostel;

    public HostelFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hostel, container, false);

        addPost = view.findViewById(R.id.add_post);
        emptyFeed = view.findViewById(R.id.empty_feed);
        contentRecyclerHostel = view.findViewById(R.id.content_recycler_hostel);
        firebaseAuth = FirebaseAuth.getInstance();
        mBase = userRef =  FirebaseDatabase.getInstance().getReference();
        mBase.keepSynced(true);
        contentRecyclerHostel.setHasFixedSize(true);
        contentRecyclerHostel.setLayoutManager(new LinearLayoutManager(getContext()));
        contentRecyclerHostel.setItemAnimator(new DefaultItemAnimator());

        userRef.child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String hostel = snapshot.child("Hostel").getValue().toString();
                showPost(hostel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPostHostelActivity.class));
            }
        });
        return view;
    }

    private void showPost(String hostel) {
        mBase.child("Post").child("Hostel").child(hostel).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hostelFeed = new ArrayList<>();
                if(snapshot.getChildrenCount() <= 0) {
                    emptyFeed.setVisibility(View.VISIBLE);
                } else {
                    emptyFeed.setVisibility(View.GONE);
                }

                for(DataSnapshot feedSnap : snapshot.getChildren()) {
                    String post = feedSnap.child("post").getValue().toString();
                    String posterName =  feedSnap.child("posterName").getValue().toString();
                    String posterDept = feedSnap.child("posterDept").getValue().toString();
                    String date = feedSnap.child("date").getValue().toString();
                    String time = feedSnap.child("time").getValue().toString();
                    String posterUID= feedSnap.child("posterUID").getValue().toString();
                    hostelFeed.add(new Hostel(post, posterUID, posterName, posterDept, date,time));
                    int count=0;
                    count++;

                }
                hostelAdapter = new HostelAdapter(getContext(), hostelFeed);
                contentRecyclerHostel.setAdapter(hostelAdapter);

                //Log.i("Populated List",campusFeed.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
