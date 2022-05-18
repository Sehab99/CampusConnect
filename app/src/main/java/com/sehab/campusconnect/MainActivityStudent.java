package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehab.campusconnect.Fragments.CampusFragment;
import com.sehab.campusconnect.Fragments.DepartmentFragment;
import com.sehab.campusconnect.Fragments.EventsFragment;
import com.sehab.campusconnect.Fragments.HostelFragment;

public class MainActivityStudent extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String uid;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        getSupportActionBar().setTitle("Campus");

        firebaseAuth = FirebaseAuth.getInstance();
        uid =firebaseAuth.getUid().toString();
        mBase = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        mBase.keepSynced(true);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = new CampusFragment();

                switch(item.getItemId()) {
                    case R.id.nav_campus:
                        selectedFragment = new CampusFragment();
                        getSupportActionBar().setTitle("Campus");
                        break;

                    case R.id.nav_department:
                        selectedFragment = new DepartmentFragment();
                        getSupportActionBar().setTitle("Department");
                        break;

                    case R.id.nav_hostel:
                        selectedFragment = new HostelFragment();
                        getSupportActionBar().setTitle("Hostel");
                        break;

                    case R.id.nav_event:
                        selectedFragment = new EventsFragment();
                        getSupportActionBar().setTitle("Event");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace
                        (R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace
                (R.id.fragment_container, new CampusFragment()).commit();

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
                startActivity(new Intent(MainActivityStudent.this, ProfileActivity.class));
                break;

            case R.id.aboutUs:
                startActivity(new Intent(MainActivityStudent.this, AboutUsActivity.class));
                break;

            case R.id.talkMode:
                break;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivityStudent.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivityStudent.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}