package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehab.campusconnect.adapters.DepartmentAdapter;
import com.sehab.campusconnect.models.Department;

import java.util.ArrayList;
import java.util.Collections;

public class GroupFeedActivity extends AppCompatActivity {
    TextView textViewGroupKey;
    Button buttonCopy;
    String groupKey;
    FloatingActionButton addPost;
    TextView emptyFeed;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    DepartmentAdapter groupAdapter;
    ArrayList<Department> groupFeed;
    RecyclerView contentRecyclerGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feed);

        groupKey = getIntent().getExtras().getString("groupKey");
        textViewGroupKey = findViewById(R.id.text_view_group_key);
        buttonCopy = findViewById(R.id.button_copy);

        addPost = findViewById(R.id.add_post);
        emptyFeed = findViewById(R.id.empty_feed);
        contentRecyclerGroup = findViewById(R.id.content_recycler_group);
        contentRecyclerGroup.setHasFixedSize(true);
        contentRecyclerGroup.setLayoutManager(new LinearLayoutManager(GroupFeedActivity.this));
        contentRecyclerGroup.setItemAnimator(new DefaultItemAnimator());

        firebaseAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference("Group").child(groupKey).child("Post");
        mBase.keepSynced(true);

        textViewGroupKey.setText(groupKey);
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Group Key", textViewGroupKey.getText().toString());
                clipboard.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(GroupFeedActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });

        mBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupFeed = new ArrayList<>();
                if(snapshot.getChildrenCount() <= 0) {
                    emptyFeed.setVisibility(View.VISIBLE);
                } else {
                    emptyFeed.setVisibility(View.GONE);
                }

                for(DataSnapshot feedSnap : snapshot.getChildren()) {
                    String post = feedSnap.child("post").getValue().toString();
                    String posterName =  feedSnap.child("posterName").getValue().toString();
                    String posterHostel = feedSnap.child("posterHostel").getValue().toString();
                    String date = feedSnap.child("date").getValue().toString();
                    String time = feedSnap.child("time").getValue().toString();
                    String posterUID= feedSnap.child("posterUID").getValue().toString();
                    groupFeed.add(new Department(post, posterName, posterHostel, date, time, posterUID));
                }
                Collections.reverse(groupFeed);
                groupAdapter = new DepartmentAdapter(GroupFeedActivity.this, groupFeed);
                contentRecyclerGroup.setAdapter((groupAdapter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupFeedActivity.this,
                        AddPostDepartmentActivity.class));
                finish();
            }
        });
    }
}