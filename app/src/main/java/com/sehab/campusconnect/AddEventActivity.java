package com.sehab.campusconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class AddEventActivity extends AppCompatActivity {
    private Button buttonDatePicker;
    private Button buttonTimePicker;
    private Button buttonAddEvent;
    private TextView textViewEventDate;
    private TextView textViewEventTime;
    private TextView textViewEventDesc;
    private TextInputLayout textInputEventName;
    String eventDate, eventTime;
    int hour, min;

    HashMap<String, Object> newEvent;
    FirebaseAuth firebaseAuth;
    DatabaseReference mBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setTitle(" ");

        textInputEventName = findViewById(R.id.text_input_event_name);
        buttonDatePicker = findViewById(R.id.button_date_picker);
        textViewEventDate = findViewById(R.id.text_view_event_date);
        buttonTimePicker = findViewById(R.id.button_time_picker);
        textViewEventTime = findViewById(R.id.text_view_event_time);
        textViewEventDesc = findViewById(R.id.text_view_event_desc);
        buttonAddEvent = findViewById(R.id.button_add_event);

        newEvent = new HashMap<>();
        firebaseAuth =FirebaseAuth.getInstance();
        mBase = FirebaseDatabase.getInstance().getReference();

        //CalendarConstraints
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        calendar.setTimeInMillis(today);
        CalendarConstraints.Builder constrainBuilder = new CalendarConstraints.Builder();
        constrainBuilder.setValidator(DateValidatorPointForward.now());

        //MaterialDatePicker
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Event Date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constrainBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "Date_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                eventDate = materialDatePicker.getHeaderText();
                textViewEventDate.setText(eventDate);
            }
        });

        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                calendar.set(0,0,0,hour,min);
                                eventTime = (String) DateFormat.format("hh:mm aa", calendar);
                                textViewEventTime.setText(eventTime);
                            }
                        }, 12, 0, false
                );
                timePickerDialog.updateTime(hour, min);
                timePickerDialog.show();
            }
        });

        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = textInputEventName.getEditText().getText().toString();
                String eventDesc = textViewEventDesc.getText().toString();
                String posterUID = firebaseAuth.getUid();
//                Toast.makeText(AddEventActivity.this, eventName + " " + date + " " +
//                        time + " " + eventDesc, Toast.LENGTH_SHORT).show();

                mBase.child("Users").child(posterUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String posterName = snapshot.child("Full Name").getValue().toString();
                        String posterDept = snapshot.child("Department").getValue().toString();

                        if (eventName.isEmpty() || eventDesc.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
                            Toast.makeText(AddEventActivity.this, "Give all information", Toast.LENGTH_SHORT).show();
                        } else {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMM yy");
                            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm");
                            String date = sdfDate.format(calendar.getTime());
                            String time = sdfTime.format(calendar.getTime());
                            newEvent.put("posterName", posterName);
                            newEvent.put("posterDept", posterDept);
                            newEvent.put("date", date);
                            newEvent.put("time", time);
                            newEvent.put("eventName", eventName);
                            newEvent.put("eventDesc", eventDesc);
                            newEvent.put("eventDate", eventDate);
                            newEvent.put("eventTime", eventTime);
                            newEvent.put("posterUID", posterUID);

                            addEvent();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void addEvent() {
        mBase.child("Post").child("Event").push().updateChildren(newEvent)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(AddEventActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddEventActivity.this, MainActivityStudent.class));
                    finish();
                } else {
                    Toast.makeText(AddEventActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}