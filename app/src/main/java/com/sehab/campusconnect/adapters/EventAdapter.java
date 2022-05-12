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
import com.sehab.campusconnect.models.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    Context context;
    ArrayList<Event> eventsArrayList;

    public EventAdapter(Context context, ArrayList<Event> eventsArrayList) {
        this.context = context;
        this.eventsArrayList = eventsArrayList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_event, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event events = eventsArrayList.get(position);
        holder.eventName.setText(events.getEventName());
        holder.eventDate.setText(events.getEventDate());
        holder.eventTime.setText(events.getEventTime());
        holder.eventDesc.setText(events.getEventDesc());
        holder.posterName.setText(events.getPosterName());
        holder.posterDept.setText(events.getPosterDept());
        holder.date.setText(events.getDate());
        holder.time.setText(events.getTime());
    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventDate;
        TextView eventTime;
        TextView eventDesc;
        TextView posterName;
        TextView posterDept;
        TextView date;
        TextView time;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDesc = itemView.findViewById(R.id.event_desc);
            posterName = itemView.findViewById(R.id.poster_name);
            posterDept = itemView.findViewById(R.id.poster_dept);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
}
