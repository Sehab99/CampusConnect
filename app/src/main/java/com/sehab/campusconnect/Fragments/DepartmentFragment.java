package com.sehab.campusconnect.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehab.campusconnect.DepartmentMainFeedActivity;
import com.sehab.campusconnect.R;

import java.util.HashMap;
import java.util.Objects;

public class DepartmentFragment extends Fragment {
    private FloatingActionButton addPost;
    private TextView emptyFeed;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mBase;
    private RelativeLayout departmentMain;
    private TextView departmentName;
    private String DEPARTMENT;

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

        mBase.child("Users").child(Objects.requireNonNull(firebaseAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DEPARTMENT = snapshot.child("Department").getValue().toString();
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
                startActivity(new Intent(getContext(), DepartmentMainFeedActivity.class)
                        .putExtra("Department Name", DEPARTMENT));
            }
        });


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBase.child("Users").child(Objects.requireNonNull(firebaseAuth.getUid()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String TYPE = snapshot.child("Type").getValue().toString();
                        String NAME = snapshot.child("Full Name").getValue().toString();
                        if(TYPE.equals("Student")) {
                            joinNewGroup(firebaseAuth, mBase, NAME);
                        } else {
                            createNewGroup(firebaseAuth, mBase, NAME);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });
        return view;
    }

    private void createNewGroup(FirebaseAuth firebaseAuth, DatabaseReference mBase, String NAME) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
        builder.setTitle("Create new group");
        final View LAYOUT = getLayoutInflater().inflate(R.layout.custom_layout_create_group,null);
        builder.setView(LAYOUT);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputLayout textInputGroupName = LAYOUT.findViewById(R.id.text_input_group_name);
                String groupName = textInputGroupName.getEditText().getText().toString();
                String uID = firebaseAuth.getUid();
                DatabaseReference groupIDReference =mBase.push();
                String groupID = groupIDReference.getKey();
                HashMap<String, Object> groupInfo = new HashMap<>();
                HashMap<String, Object> userTree = new HashMap<>();

                groupInfo.put("Group Name", groupName);
                groupInfo.put("Creator Name", NAME);
                groupInfo.put("Creator ID", uID);

                userTree.put("Group Name", groupName);

                mBase.child("Group").child(groupID).updateChildren(groupInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        if(task1.isSuccessful()) {
                            mBase.child("Users").child(uID).child("Groups Created").child(groupID)
                                    .updateChildren(userTree)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task2) {
                                    if(task2.isSuccessful()) {
                                        Toast.makeText(getContext(), groupName + "Created", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void joinNewGroup(FirebaseAuth firebaseAuth, DatabaseReference mBase, String NAME) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
        builder.setTitle("Join new group");
        final View LAYOUT = getLayoutInflater().inflate(R.layout.custom_layout_join_group,null);
        builder.setView(LAYOUT);
        builder.setPositiveButton("Join", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextInputLayout textInputJoiningLink = LAYOUT.findViewById(R.id.text_input_group_link);
                String joiningLink = textInputJoiningLink.getEditText().getText().toString();

                mBase.child("Group").child(joiningLink).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String uID = firebaseAuth.getUid();
                            HashMap<String, Object> memberTree = new HashMap<>();
                            memberTree.put("Name", NAME);
                            HashMap<String, Object> userTree = new HashMap<>();

                            mBase.child("Group").child(joiningLink)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String groupName = snapshot.child("Group Name").getValue().toString();
                                            userTree.put("Group Name", groupName);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                            mBase.child("Group").child(joiningLink).child("Members").child(uID)
                                    .updateChildren(memberTree)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task1) {
                                            if(task1.isSuccessful()) {
                                                mBase.child("Users").child(uID).child("Groups Joined").child(joiningLink)
                                                        .updateChildren(userTree).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task2) {
                                                                if(task2.isComplete()) {
                                                                    Toast.makeText(getContext(), "Joined", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Group not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//                String uID = firebaseAuth.getUid();
//                HashMap<String, Object> memberTree = new HashMap<>();
//                memberTree.put("Name", NAME);
//                HashMap<String, Object> userTree = new HashMap<>();
//
//                mBase.child("Group").child(joiningLink)
//                        .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String groupName = snapshot.child("Group Name").getValue().toString();
//                        userTree.put("Group Name", groupName);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//                mBase.child("Group").child(joiningLink).child("Members").child(uID)
//                        .updateChildren(memberTree)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task1) {
//                        if(task1.isSuccessful()) {
//                            mBase.child("Users").child(uID).child("Groups Joined").child(joiningLink)
//                                    .updateChildren(userTree).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task2) {
//                                            if(task2.isComplete()) {
//                                                Toast.makeText(getContext(), "Joined", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                        } else {
//                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
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
