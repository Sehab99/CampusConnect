package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityFaculty extends AppCompatActivity {
    String uid;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_faculty);

        firebaseAuth = FirebaseAuth.getInstance();
        uid =firebaseAuth.getUid().toString();
        mBase = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        mBase.keepSynced(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MainActivityFaculty.this, ProfileActivity.class));
                break;

            case R.id.aboutUs:
                startActivity(new Intent(MainActivityFaculty.this, AboutUsActivity.class));
                break;

            case R.id.talkMode:
                break;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivityFaculty.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivityFaculty.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}