package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehab.campusconnect.adapters.MemberAdapter;
import com.sehab.campusconnect.models.Members;

import java.util.ArrayList;

public class Group_Member_List_Activity extends AppCompatActivity {
    RecyclerView recyclerViewMemberList;
    DatabaseReference databaseReference;
    String groupLink;
    TextView emptyGroup;
    MemberAdapter adapter;
    ArrayList<Members> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_list);
        getSupportActionBar().setTitle("Group Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        groupLink = getIntent().getStringExtra("Group Link");
        emptyGroup = findViewById(R.id.empty_group);
        recyclerViewMemberList = findViewById(R.id.member_list);
        recyclerViewMemberList.setHasFixedSize(true);
        recyclerViewMemberList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMemberList.setItemAnimator(new DefaultItemAnimator());

        databaseReference = FirebaseDatabase.getInstance().getReference("Group").child(groupLink).child("Members");
        members = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() <= 0) {
                    emptyGroup.setVisibility(View.VISIBLE);
                } else {
                    emptyGroup.setVisibility(View.GONE);
                }

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String memberName = userSnap.child("Name").getValue().toString();
                    members.add(new Members(memberName));
                }
                adapter = new MemberAdapter(Group_Member_List_Activity.this, members);
                recyclerViewMemberList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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