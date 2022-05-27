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
import com.sehab.campusconnect.models.Campus;

import java.util.ArrayList;
import java.util.Locale;

public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.CampusViewHolder> {
    Context context;
    ArrayList<Campus> campusArrayList;
    TextToSpeech textToSpeech;

    public CampusAdapter(Context context, ArrayList<Campus> campusArrayList) {
        this.context = context;
        this.campusArrayList = campusArrayList;
    }

    @NonNull
    @Override
    public CampusAdapter.CampusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_post, parent, false);
        CampusViewHolder viewHolder = new CampusViewHolder(view);

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
    public void onBindViewHolder(@NonNull CampusAdapter.CampusViewHolder holder, int position) {
        Campus campus = campusArrayList.get(position);
        holder.profileName.setText(campus.getPosterName());
        holder.post.setText(campus.getPost());
        holder.deptName.setText(campus.getPosterDept());
        holder.date.setText(campus.getDate());
        holder.time.setText(campus.getTime());
        holder.cardContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(campus.getPost() + "        " + "Posted by" + campus.getPosterName() +
                        campus.getPosterDept(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campusArrayList.size();
    }

    public static class CampusViewHolder extends RecyclerView.ViewHolder {
        TextView profileName;
        TextView post;
        TextView deptName;
        TextView date;
        TextView time;
        RelativeLayout cardContent;
        public CampusViewHolder(@NonNull View itemView) {
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
