package com.sehab.campusconnect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class AddPost extends AppCompatActivity {

    private ImageButton closeImageButton;
    private Button postContentButton;
    private ImageView profilePic;
    private Spinner spinnerPostScope;
    private EditText writeContentEditText;
    private EditText postLogEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().hide();

        spinnerPostScope = (Spinner) findViewById(R.id.spinner_post_scope);
        ArrayAdapter<CharSequence> postScopeAdapter = ArrayAdapter.createFromResource
                (this, R.array.Fragments, android.R.layout.simple_spinner_item);
        postScopeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPostScope.setAdapter(postScopeAdapter);

        closeImageButton = findViewById(R.id.close_image_button);
        writeContentEditText = findViewById(R.id.write_content);

        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String writeContent = writeContentEditText.getText().toString();

                if(writeContent == null) {
                    startActivity(new Intent(AddPost.this, MainActivity.class));
                    finish();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddPost.this);
                    alertDialog.setTitle("Close post?");
                    alertDialog.setMessage("You have written something, by closing you will lose your post." +
                            "/nAre you sure you want to close?");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(AddPost.this, MainActivity.class));
                            finish();
                        }
                    });
                    alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
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
}