package com.sehab.campusconnect.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehab.campusconnect.AddPostCampusActivity;
import com.sehab.campusconnect.AddPostFacultyActivity;
import com.sehab.campusconnect.R;
import com.sehab.campusconnect.adapters.FacultyAdapter;
import com.sehab.campusconnect.models.Faculty;

import java.util.ArrayList;
import java.util.Collections;

public class FacultyFragment extends Fragment {
    FloatingActionButton addPost;
    TextView emptyFeed;

    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    FacultyAdapter facultyAdapter;
    ArrayList<Faculty> facultyFeed;
    RecyclerView contentRecyclerFaculty;

    public FacultyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);

        addPost = view.findViewById(R.id.add_post);
        emptyFeed = view.findViewById(R.id.empty_feed);
        contentRecyclerFaculty = view.findViewById(R.id.content_recycler_faculty);
        firebaseAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference("Post").child("Faculty");
        mBase.keepSynced(true);
        contentRecyclerFaculty.setHasFixedSize(true);
        contentRecyclerFaculty.setLayoutManager(new LinearLayoutManager(getContext()));
        contentRecyclerFaculty.setItemAnimator(new DefaultItemAnimator());

        mBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                facultyFeed = new ArrayList<>();
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
                    facultyFeed.add(new Faculty(post, posterUID, posterName, posterDept, date,time));
                }
                Collections.reverse(facultyFeed);
                facultyAdapter = new FacultyAdapter(getContext(), facultyFeed);
                contentRecyclerFaculty.setAdapter(facultyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddPostFacultyActivity.class));
            }
        });
        return view;
    }
}