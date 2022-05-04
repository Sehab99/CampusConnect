package com.sehab.campusconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sehab.campusconnect.R;
import com.sehab.campusconnect.models.Campus;

import java.util.ArrayList;

public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.CampusViewHolder> {
    Context context;
    ArrayList<Campus> campusArrayList;

    public CampusAdapter(Context context, ArrayList<Campus> campusArrayList) {
        this.context = context;
        this.campusArrayList = campusArrayList;
    }

    @NonNull
    @Override
    public CampusAdapter.CampusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_post, parent, false);
        CampusViewHolder viewHolder = new CampusViewHolder(view);
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

        public CampusViewHolder(@NonNull View itemView) {
            super(itemView);
            profileName = itemView.findViewById(R.id.profile_name);
            post = itemView.findViewById(R.id.post_content);
            deptName = itemView.findViewById(R.id.profile_dept);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
}
