package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputLoginPassword;
    private Button buttonLogin;
    private Button buttonToSignUp;
    private Button buttonForgotPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login to Continue");

        textInputUsername = findViewById(R.id.text_input_username);
        textInputLoginPassword = findViewById(R.id.text_input_login_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonToSignUp = findViewById(R.id.button_to_sign_up);
        buttonForgotPassword = findViewById(R.id.button_forgot_password);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference().child("Users");

        final AutoCompleteTextView userTextView = findViewById(R.id.user_text_view);
        ArrayAdapter<CharSequence> userAdapter = ArrayAdapter.createFromResource
                (this, R.array.Users, android.R.layout.simple_spinner_item);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTextView.setAdapter(userAdapter);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textInputUsername.getEditText().getText().toString();
                String password = textInputLoginPassword.getEditText().getText().toString();
                String user = userTextView.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(username, password, user);
                }
            }
        });

        buttonToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    private void loginUser(String username, String password, String user) {
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    userRef.child(task.getResult().getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String user_type =  snapshot.child("Type").getValue().toString();
                            if(user.equals("Student")) {
                                if(user_type.equals(user)) {
                                    Toast.makeText(LoginActivity.this, "Login Successful as Student", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivityStudent.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Change user type and Login again", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if(user_type.equals(user)) {
                                    Toast.makeText(LoginActivity.this, "Login Successful as Teacher", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivityFaculty.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Change user type and Login again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong credential", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}