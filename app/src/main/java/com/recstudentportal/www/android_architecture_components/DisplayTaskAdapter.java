package com.recstudentportal.www.android_architecture_components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class DisplayTaskAdapter extends RecyclerView.Adapter<DisplayTaskAdapter.TaskViewHolder> {

    Context context;
    List<TaskEntry> entries;
    ItemClickListener itemClickListener;
    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    public DisplayTaskAdapter(Context c,ItemClickListener listener) {
        context=c;
        itemClickListener=listener;
       // entries=taskEntries;
    }
    public void getTasks(List<TaskEntry> taskEntries) {
        entries=taskEntries;
        notifyDataSetChanged();
    }
    public List<TaskEntry> findTasks(){
        return entries;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.task_layout,parent,false);
        return new TaskViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if(entries.get(position).getDescription()!=null) {
            holder.taskDescriptions.setText(entries.get(position).getDescription());
            holder.taskUpdatedAts.setText(entries.get(position).getUpdatedAt().toString());
            holder.priorityTextViews.setText(Integer.toString( entries.get(position).getPriority()));
        }
    }

    @Override
    public int getItemCount() {
        if(entries!=null)
        return entries.size();
        else return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView taskDescriptions,taskUpdatedAts,priorityTextViews;
        public TaskViewHolder(View itemView) {
            super(itemView);
            taskDescriptions=(TextView)itemView.findViewById(R.id.taskDescription);
            taskUpdatedAts=(TextView)itemView.findViewById(R.id.taskUpdatedAt);
            priorityTextViews=(TextView) itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = entries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClickListener(elementId);
        }
    }
}
