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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout textInputName;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputRegID;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputConformPassword;
    private TextInputLayout textInputBatch;
    private TextInputLayout textInputHomeAddress;
    private Button buttonSignUp;
    private Button buttonToLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private HashMap<String, Object> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign UP");

        textInputName = findViewById(R.id.text_input_name);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputRegID = findViewById(R.id.text_input_regID);
        textInputBatch = findViewById(R.id.text_input_batch);
        textInputHomeAddress = findViewById(R.id.text_input_home_address);
        textInputPassword = findViewById(R.id.text_input_password);
        textInputConformPassword = findViewById(R.id.text_input_conform_password);
        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonToLogin = findViewById(R.id.button_to_login);

        final AutoCompleteTextView departmentTextView = findViewById(R.id.department_text_view);
        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource
                (this, R.array.Department, android.R.layout.simple_spinner_item);
        departmentTextView.setAdapter(departmentAdapter);

        final AutoCompleteTextView hostelTextView = findViewById(R.id.hostel_text_view);
        ArrayAdapter<CharSequence> hostelAdapter = ArrayAdapter.createFromResource
                (this, R.array.Hostel, android.R.layout.simple_spinner_item);
        hostelTextView.setAdapter(hostelAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");
        userData = new HashMap<>();


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = textInputName.getEditText().getText().toString();
                String email = textInputEmail.getEditText().getText().toString();
                String regID = textInputRegID.getEditText().getText().toString();
                String department = departmentTextView.getText().toString();
                String hostel = hostelTextView.getText().toString();
                String batch = textInputBatch.getEditText().getText().toString();
                String homeAddress = textInputHomeAddress.getEditText().getText().toString();
                String password = textInputPassword.getEditText().getText().toString();
                String conformPassword = textInputConformPassword.getEditText().getText().toString();
                String type = "Student";

                if(TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(regID)
                        || TextUtils.isEmpty(department) || TextUtils.isEmpty(hostel) ||
                        TextUtils.isEmpty(batch) || TextUtils.isEmpty(homeAddress) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(conformPassword)) {
                    Toast.makeText(SignUpActivity.this, "Empty Credential", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password should be minimum 6 character", Toast.LENGTH_SHORT).show();
                } else if(!password.equals(conformPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password mismatch", Toast.LENGTH_SHORT).show();
                } else {
                    userData.put("Full Name", fullName);
                    userData.put("Email", email);
                    userData.put("RegID", regID);
                    userData.put("Department", department);
                    userData.put("Hostel", hostel);
                    userData.put("Batch", batch);
                    userData.put("Home Address", homeAddress);
                    userData.put("Type", type);
                    signUpUser(email, password);
                }
            }
        });

        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void signUpUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    String userID = task.getResult().getUser().getUid();
                    databaseReference.child(userID).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> t) {
                            if(t.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "SignUp completed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "SignUp failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "SignUp failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}