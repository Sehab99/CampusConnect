package com.sehab.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;

public class AddEventActivity extends AppCompatActivity {
    private Button buttonDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        buttonDatePicker = findViewById(R.id.button_date_picker);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Event Date");
        final MaterialDatePicker materialDatePicker = builder.build();

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "Date_PICKER");
                //Toast.makeText(AddEventActivity.this, materialDatePicker.getHeaderText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}