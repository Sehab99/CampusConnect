package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private TextView textViewProfileName;
    private TextView textViewBatch;
    private TextView textViewDepartmentName;
    private TextView textViewHostelName;
    private TextView textViewRegNumber;
    private TextView textViewEmailID;
    private TextView textViewHomeAddress;
    private ImageView backFromProfile;
    private CircleImageView profilePicture;
    private ImageButton chooseImage;
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mBase;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        backFromProfile = findViewById(R.id.back_from_profile);
        textViewProfileName = findViewById(R.id.profile_name);
        textViewBatch = findViewById(R.id.batch);
        textViewDepartmentName = findViewById(R.id.department_name);
        textViewHostelName = findViewById(R.id.hostel_name);
        textViewRegNumber = findViewById(R.id.reg_number);
        textViewEmailID = findViewById(R.id.email_id);
        textViewHomeAddress = findViewById(R.id.home_address);
        profilePicture = findViewById(R.id.profile_pic);
        chooseImage = findViewById(R.id.choose_image);

        firebaseAuth = FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        mBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileName = snapshot.child("Full Name").getValue().toString();
                String batch = snapshot.child("Batch").getValue().toString();
                String departmentName = snapshot.child("Department").getValue().toString();
                String hostelName = snapshot.child("Hostel").getValue().toString();
                String regNumber = snapshot.child("RegID").getValue().toString();
                String emailID = snapshot.child("Email").getValue().toString();
                String homeAddress = snapshot.child("Home Address").getValue().toString();
                textViewProfileName.setText(profileName);
                textViewBatch.setText(batch);
                textViewDepartmentName.setText(departmentName);
                textViewHostelName.setText(hostelName);
                textViewRegNumber.setText(regNumber);
                textViewEmailID.setText(emailID);
                textViewHomeAddress.setText(homeAddress);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            uri = data.getData();
            profilePicture.setImageURI(uri);
        }
    }
}