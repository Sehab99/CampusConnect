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
import com.sehab.campusconnect.models.Department;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    Context context;
    ArrayList<Department> departmentArrayList;
    TextToSpeech textToSpeech;

    public DepartmentAdapter(Context context, ArrayList<Department> departmentArrayList) {
        this.context = context;
        this.departmentArrayList = departmentArrayList;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_post, parent, false);
        DepartmentAdapter.DepartmentViewHolder viewHolder = new DepartmentAdapter.DepartmentViewHolder(view);
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
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Department department = departmentArrayList.get(position);
        holder.profileName.setText(department.getPosterName());
        holder.post.setText(department.getPost());
        holder.hostelName.setText(department.getPosterHostel());
        holder.date.setText(department.getDate());
        holder.time.setText(department.getTime());
        holder.cardContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(department.getPost() + "        " + "Posted by" +
                        department.getPosterName() + department.getPosterHostel(),
                        TextToSpeech.QUEUE_FLUSH,null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentArrayList.size();
    }


    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        TextView profileName;
        TextView post;
        TextView hostelName;
        TextView date;
        TextView time;
        RelativeLayout cardContent;
        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            post = itemView.findViewById(R.id.post_content);
            hostelName = itemView.findViewById(R.id.profile_dept);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            cardContent = itemView.findViewById(R.id.card_content);
        }
    }
}
