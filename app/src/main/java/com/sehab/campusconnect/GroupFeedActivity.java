package com.sehab.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GroupFeedActivity extends AppCompatActivity {
    TextView textViewGroupKey;
    Button buttonCopy;
    String groupKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_feed);

        groupKey = getIntent().getExtras().getString("groupKey");
        textViewGroupKey = findViewById(R.id.text_view_group_key);
        buttonCopy = findViewById(R.id.button_copy);

        textViewGroupKey.setText(groupKey);
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Group Key", textViewGroupKey.getText().toString());
                clipboard.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(GroupFeedActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }
}