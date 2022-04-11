package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addPost;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Campus");

        addPost = findViewById(R.id.add_post);
        firebaseAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddPost.class));
            }
        });

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
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.aboutUs:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                break;

            case R.id.talkMode:
                break;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}