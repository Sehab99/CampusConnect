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
import com.sehab.campusconnect.models.Members;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MembersViewHolder> {
    Context context;
    ArrayList<Members> arrayListMembers;

    public MemberAdapter(Context context, ArrayList<Members> arrayListMembers) {
        this.context = context;
        this.arrayListMembers = arrayListMembers;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_members_list, parent, false);
        MembersViewHolder membersViewHolder = new MembersViewHolder(view);
        return membersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        Members member = arrayListMembers.get(position);
        holder.memberName.setText(member.memberName);
    }

    @Override
    public int getItemCount() {
        return arrayListMembers.size();
    }

    public class MembersViewHolder extends RecyclerView.ViewHolder {
        TextView memberName;
        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById((R.id.member_name));
        }
    }
}
