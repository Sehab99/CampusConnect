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
import com.sehab.campusconnect.models.Hostel;

import java.util.ArrayList;
import java.util.Locale;

public class HostelAdapter extends RecyclerView.Adapter<HostelAdapter.HostelViewHolder> {
    Context context;
    ArrayList<Hostel> hostelArrayList;
    TextToSpeech textToSpeech;

    public HostelAdapter(Context context, ArrayList<Hostel> hostelArrayList) {
        this.context = context;
        this.hostelArrayList = hostelArrayList;
    }

    @NonNull
    @Override
    public HostelAdapter.HostelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_post, parent, false);
        HostelAdapter.HostelViewHolder viewHolder = new HostelAdapter.HostelViewHolder(view);
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
    public void onBindViewHolder(@NonNull HostelAdapter.HostelViewHolder holder, int position) {
        Hostel hostel = hostelArrayList.get(position);
        holder.profileName.setText(hostel.getPosterName());
        holder.post.setText(hostel.getPost());
        holder.deptName.setText(hostel.getPosterDept());
        holder.date.setText(hostel.getDate());
        holder.time.setText(hostel.getTime());
        holder.cardContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(hostel.getPost() + "        " + "Posted by" +
                                hostel.getPosterName() + hostel.getPosterDept(),
                        TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hostelArrayList.size();
    }

    public static class HostelViewHolder extends RecyclerView.ViewHolder {
        TextView profileName;
        TextView post;
        TextView deptName;
        TextView date;
        TextView time;
        RelativeLayout cardContent;
        public HostelViewHolder(@NonNull View itemView) {
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
