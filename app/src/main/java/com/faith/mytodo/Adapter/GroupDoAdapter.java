package com.faith.mytodo.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.AddNewTasks;
import com.faith.mytodo.R;
import com.faith.mytodo.TaskGroup;
import com.faith.mytodo.Model.ToDoModel;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class GroupDoAdapter extends RecyclerView.Adapter<GroupDoAdapter.MyViewHolder> {
    private List<ToDoModel> mytodo;
    public Activity activity;
    private FragmentManager fragmentManager;
    private String groupName;
    private OnItemClickListener itemClickListener;

    public GroupDoAdapter(Activity activity, FragmentManager fragmentManager, List<ToDoModel> mytodo, String groupName, OnItemClickListener itemClickListener) {
        this.mytodo = mytodo;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.groupName = groupName;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = mytodo.get(position);
        holder.mcheckbox.setText(toDoModel.getTask());
        holder.mstaricon.setVisibility(View.VISIBLE);

        holder.mcheckbox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Perform action when checkbox is checked
                } else {
                    // Perform action when checkbox is unchecked
                }
            }
        });

        // Set the click listener for the category item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onCategoryClick(toDoModel); // Pass the clicked ToDoModel to the OnItemClickListener
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }


    @Override
    public int getItemCount() {
        return mytodo.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mcheckbox;
        ImageView mstaricon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mcheckbox = itemView.findViewById(R.id.mcheckbox);
            mstaricon = itemView.findViewById(R.id.star_icon);
        }
    }

    // Interface for item click events
    public interface OnItemClickListener {
        void onCategoryClick(ToDoModel toDoModel);
    }
}
