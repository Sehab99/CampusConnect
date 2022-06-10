package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import java.util.Objects;

public class DepartmentFeedActivity extends AppCompatActivity {
    FloatingActionButton addPost;
    TextView emptyFeed;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    DepartmentAdapter departmentAdapter;
    ArrayList<Department> departmentFeed;
    RecyclerView contentRecyclerDepartment;

    String department;

    public DepartmentFeedActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_feed);
        department = getIntent().getStringExtra("Department Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(department);

        addPost = findViewById(R.id.add_post);
        emptyFeed = findViewById(R.id.empty_feed);
        contentRecyclerDepartment = findViewById(R.id.content_recycler_department_main);
        contentRecyclerDepartment.setHasFixedSize(true);
        contentRecyclerDepartment.setLayoutManager(new LinearLayoutManager(DepartmentFeedActivity.this));
        contentRecyclerDepartment.setItemAnimator(new DefaultItemAnimator());

        firebaseAuth = FirebaseAuth.getInstance();
//        Log.i("DEPARTMENT_LOG",department);
        mBase = FirebaseDatabase.getInstance().getReference("Post").child("Department").child(department);
        mBase.keepSynced(true);

        mBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                departmentFeed = new ArrayList<>();
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
                    departmentFeed.add(new Department(post, posterName, posterHostel, date, time, posterUID));
                }
                Collections.reverse(departmentFeed);
                departmentAdapter = new DepartmentAdapter(DepartmentFeedActivity.this, departmentFeed);
                contentRecyclerDepartment.setAdapter((departmentAdapter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepartmentFeedActivity.this,
                        AddPostDepartmentActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}