package com.sehab.campusconnect.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehab.campusconnect.AddPostCampusActivity;
import com.sehab.campusconnect.DepartmentMainFeedActivity;
import com.sehab.campusconnect.R;

public class DepartmentFragment extends Fragment {
    FloatingActionButton addPost;
    TextView emptyFeed;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;
    RelativeLayout departmentMain;
    TextView departmentName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        addPost = view.findViewById(R.id.add_post);
        departmentMain = view.findViewById(R.id.department_main);
        emptyFeed = view.findViewById(R.id.empty_feed);
        departmentName = view.findViewById(R.id.department_name);
        firebaseAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference();

        mBase.child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String DEPARTMENT = snapshot.child("Department").getValue().toString();
                departmentName.setText(DEPARTMENT);
                departmentName.setPaintFlags(departmentName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        departmentMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DepartmentMainFeedActivity.class));
            }
        });


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewGroup();
            }
        });

        return view;
    }

    private void addNewGroup() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
        builder.setTitle("Join new group");
        final View LAYOUT = getLayoutInflater().inflate(R.layout.custom_layout_alert_dialog,null);
        builder.setView(LAYOUT);
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputLayout joiningLinkTextInput = LAYOUT.findViewById(R.id.text_input_m);
                String joiningLink = joiningLinkTextInput.getEditText().getText().toString();
                Toast.makeText(getContext(), joiningLink, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //AlertDialog alertDialog = builder.create();
        builder.show();
    }
}
