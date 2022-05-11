package com.sehab.campusconnect.Fragments;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sehab.campusconnect.AddEventActivity;
import com.sehab.campusconnect.R;

public class EventsFragment extends Fragment {
    FloatingActionButton addPost;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hostel, container, false);
        addPost = view.findViewById(R.id.add_post);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddEventActivity.class));
            }
        });
        return view;
    }
}
