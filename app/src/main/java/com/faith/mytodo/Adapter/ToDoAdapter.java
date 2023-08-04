package com.faith.mytodo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.AddNewTasks;
import com.faith.mytodo.DeletedActivity;
import com.faith.mytodo.ImportantActivity;
import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> tasksList;
    private Context context;
    public Activity activity;
    private FragmentManager fragmentManager;
    private FirebaseFirestore firestore;
    private boolean isImportantView;

    public ToDoAdapter(Context context, List<ToDoModel> tasksList, boolean isImportantView, Activity activity, FragmentManager fragmentManager) {
        this.context = context;
        this.tasksList = tasksList;
        this.isImportantView = isImportantView;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel task = tasksList.get(position);
        holder.taskCheckBox.setText(task.getTask());
        holder.taskCheckBox.setChecked(toBoolean(task.getStatus()));
        holder.starIcon.setVisibility(View.VISIBLE);
        holder.starIcon.setImageResource(task.isImportant() ? R.drawable.ic_starfilled : R.drawable.ic_star);

        holder.taskCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = holder.taskCheckBox.isChecked();
                int newStatus = isChecked ? 1 : 0;
                updateTaskStatus(task.getTask(), newStatus, task.getGroupName());

                // If the checkbox is checked and the task is deleted, move the task to DeletedActivity
                if (isChecked && task.isDeleted()) {
                    moveToDeletedActivity(task);
                }
            }
        });

        holder.starIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newImportantStatus = !task.isImportant();
                updateTaskImportance(task, newImportantStatus);
                holder.starIcon.setImageResource(newImportantStatus ? R.drawable.ic_starfilled : R.drawable.ic_star);

                // If the star icon is clicked and the task is important, move the task to ImportantActivity
                if (newImportantStatus) {
                    moveToImportantActivity(task);
                }
            }
        });
    }


    private void moveToImportantActivity(ToDoModel task) {
        Intent intent = new Intent(context, ImportantActivity.class);
        intent.putExtra("tasks", task); // Pass the task as an extra
        context.startActivity(intent);
    }

        private void moveToDeletedActivity(ToDoModel task) {
            Intent intent = new Intent(context, DeletedActivity.class);
            intent.putExtra("tasks", task); // Pass the task as an extra
            context.startActivity(intent);
        }




    private boolean toBoolean(int status) {
        return status != 0;
    }

    private void updateTaskStatus(String taskName, int newStatus, String groupName) {

    }

    private void updateTaskImportance(ToDoModel task, boolean isImportant) {

    }


    public void setTasks(List<ToDoModel> tasksList) {
        this.tasksList = tasksList;
        notifyDataSetChanged();
    }


    public void deleteTask(int position) {
        ToDoModel item = tasksList.get(position);
        tasksList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position, String n) {
        ToDoModel item = tasksList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getStatus());
        bundle.putString("task", item.getTask());

        AddNewTasks task = new AddNewTasks();
        task.setArguments(bundle);

        task.show(fragmentManager, task.getTag());
    }

    @Override
    public int getItemCount() {
        if (tasksList != null) {
            return tasksList.size();
        } else {
            return 0;
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        ImageView starIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.mcheckbox);
            starIcon = itemView.findViewById(R.id.star_icon);
        }
    }
}
