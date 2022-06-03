package com.sehab.campusconnect;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

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

    ActivityResultLauncher<Intent> cropActivity;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mBase;
    String cameraPermission[];
    String storagePermission[];
    private Uri imageuri;

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

        cropActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                CropImage.ActivityResult cropResult = CropImage.getActivityResult(result.getData());
                if (result.getResultCode() == RESULT_OK) {
                    Uri resultUri = cropResult.getUri();
                    Picasso.with(ProfileActivity.this).load(resultUri).into(profilePicture);

                }
            }
        });

        mBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileName = snapshot.child("Full Name").getValue().toString();
                String batch = snapshot.child("Batch").getValue().toString();
                String url = "";
                if (snapshot.child("URL").getValue()!=null)
                    url = snapshot.child("URL").getValue().toString();
                String departmentName = snapshot.child("Department").getValue().toString();
                String hostelName = snapshot.child("Hostel").getValue().toString();
                String regNumber = snapshot.child("RegID").getValue().toString();
                String emailID = snapshot.child("Email").getValue().toString();
                String homeAddress = snapshot.child("Home Address").getValue().toString();
                textViewProfileName.setText(profileName);
                textViewBatch.setText(batch);
                textViewDepartmentName.setText(departmentName);
                textViewHostelName.setText(hostelName);
                if (!url.equals(""))
                Picasso.with(ProfileActivity.this).load(url).into(profilePicture);
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageuri = data.getData();
            String uid = FirebaseAuth.getInstance().getUid();


            // Create a Cloud Storage reference from the app
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Users");
            ProgressDialog dialog = ProgressDialog.show(this,"Please Wait","Uploading the profile");
            storageRef.child(uid + ".png").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageRef.child(uid+".png").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String url = task.getResult().toString();
                                dialog.dismiss();
                                Picasso.with(ProfileActivity.this).load(imageuri).into(profilePicture);
                                mBase.child("URL").setValue(url);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    dialog.setMessage((int) progress + "% Uploading...");
                }
            });
        }
    }
}