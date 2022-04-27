package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=1000;
    private FirebaseAuth firebaseAuth;
    DatabaseReference userRef;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();


        firebaseAuth =FirebaseAuth.getInstance();

        uid = firebaseAuth.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Log.i(uid, "run");
                if(firebaseAuth.getCurrentUser() != null) {
//                    startActivity(new Intent(SplashActivity.this, MainActivityStudent.class));
//                    finish();
                    userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String user_type =  snapshot.child("Type").getValue().toString();
                            if (user_type.equals("Student")) {
                                startActivity(new Intent(SplashActivity.this, MainActivityStudent.class));
                                finish();
                            } else if(user_type.equals("Faculty")) {
                                startActivity(new Intent(SplashActivity.this, MainActivityFaculty.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}