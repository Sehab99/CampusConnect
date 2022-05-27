package com.sehab.campusconnect.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sehab.campusconnect.R;
import com.sehab.campusconnect.models.Faculty;

import java.util.ArrayList;
import java.util.Locale;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder>{
    Context context;
    ArrayList<Faculty> facultyArrayList;
    TextToSpeech textToSpeech;

    public FacultyAdapter(Context context, ArrayList<Faculty> facultyArrayList) {
        this.context = context;
        this.facultyArrayList = facultyArrayList;
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_post, parent, false);
        FacultyViewHolder viewHolder = new FacultyViewHolder(view);
        textToSpeech = new TextToSpeech(context.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position) {
        Faculty faculty = facultyArrayList.get(position);
        holder.profileName.setText(faculty.getPosterName());
        holder.post.setText(faculty.getPost());
        holder.deptName.setText(faculty.getPosterDept());
        holder.date.setText(faculty.getDate());
        holder.time.setText(faculty.getTime());
        holder.cardContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(faculty.getPost() + "        " + "Posted by" +
                                faculty.getPosterName() + faculty.getPosterDept(),
                        TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return facultyArrayList.size();
    }

    public class FacultyViewHolder extends RecyclerView.ViewHolder {
        TextView profileName;
        TextView post;
        TextView deptName;
        TextView date;
        TextView time;
        RelativeLayout cardContent;
        public FacultyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            post = itemView.findViewById(R.id.post_content);
            deptName = itemView.findViewById(R.id.profile_dept);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            cardContent = itemView.findViewById(R.id.card_content);
        }
    }
}
