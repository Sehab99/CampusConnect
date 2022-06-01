package com.sehab.campusconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sehab.campusconnect.GroupFeedActivity;
import com.sehab.campusconnect.R;
import com.sehab.campusconnect.models.Group;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    Context context;
    ArrayList<Group> groupArrayList;

    public GroupAdapter(Context context, ArrayList<Group> groupArrayList) {
        this.context = context;
        this.groupArrayList = groupArrayList;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_department_group,parent,false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupArrayList.get(position);
        holder.creatorName.setText(group.getCreatorName());
        holder.groupName.setText(group.getGroupName());
        holder.groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroupFeedActivity.class);
                intent.putExtra("groupKey", group.getGroupKey());
                intent.putExtra("groupName", group.getGroupName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView creatorName;
        TextView groupName;
        RelativeLayout groupView;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            creatorName = itemView.findViewById(R.id.creator_name);
            groupName = itemView.findViewById(R.id.group_name);
            groupView = itemView.findViewById(R.id.card_department_group);
        }
    }
}
