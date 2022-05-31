package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddPostGroupActivity extends AppCompatActivity {
    private ImageButton closeImageButton;
    private Button postContentButton;
    private ImageView profilePic;
    private TextView textViewPostScope;
    private TextView textViewPostLog;
    private EditText writeContentEditText;
    HashMap<String, Object> newPost;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;
    String post;
    String groupKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_group);
        getSupportActionBar().hide();
        groupKey = getIntent().getStringExtra("Group Key");

        postContentButton = findViewById(R.id.post_content);
        textViewPostScope = findViewById(R.id.post_scope);
        textViewPostLog = findViewById(R.id.post_log);
        closeImageButton = findViewById(R.id.close_image_button);
        writeContentEditText = findViewById(R.id.write_content);
        newPost = new HashMap<>();
        firebaseAuth =FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference();

        postContentButton.setEnabled(false);
        textViewPostScope.setText(groupKey);

        writeContentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty())
                    postContentButton.setEnabled(false);
                else
                if (!postContentButton.isEnabled())
                    postContentButton.setEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        postContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post = writeContentEditText.getText().toString();
                String posterUID = firebaseAuth.getUid();

                mBase.child("Users").child(posterUID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String posterName = snapshot.child("Full Name").getValue().toString();
                        String posterHostel = snapshot.child("Hostel").getValue().toString();

                        if(post.isEmpty()) {
                            Toast.makeText(AddPostGroupActivity.this, "Post is Empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate =new SimpleDateFormat("dd MMM yy");
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
                            String date = sdfDate.format(calendar.getTime());
                            String time = sdfTime.format(calendar.getTime());
                            newPost.put("date", date);
                            newPost.put("post", post);
                            newPost.put("posterHostel", posterHostel);
                            newPost.put("posterName", posterName);
                            newPost.put("time", time);
                            newPost.put("posterUID", posterUID);

                            addPost(groupKey);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post = writeContentEditText.getText().toString();
                if(post.isEmpty()) {
                    finish();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddPostGroupActivity.this);
                    alertDialog.setTitle("Close post?");
                    alertDialog.setMessage("You have written something, by closing you will lose your post." +
                            "\nAre you sure you want to close?");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        });
    }

    private void addPost(String groupKey) {
        mBase.child("Group").child(groupKey).child("Post").push().updateChildren(newPost)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(AddPostGroupActivity.this, DepartmentFeedActivity.class));
                            finish();
                            Toast.makeText(AddPostGroupActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddPostGroupActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}