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
import com.faith.mytodo.CategoryClickListener;
import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class GroupDoAdapter extends RecyclerView.Adapter<GroupDoAdapter.MyViewHolder> {
    private List<ToDoModel> mytodo;
    public Activity activity;
    private FragmentManager fragmentManager;
    private String groupName;
    private CategoryClickListener categoryClickListener;

    public GroupDoAdapter(Activity activity, FragmentManager fragmentManager, List<ToDoModel> mytodo, String groupName, CategoryClickListener categoryClickListener) {
        this.mytodo = mytodo;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.groupName = groupName;
        this.categoryClickListener = categoryClickListener;
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
                categoryClickListener.onCategoryClick(toDoModel); // Pass the clicked ToDoModel to the CategoryClickListener
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    public void setTasks(List<ToDoModel> mytodo) {
        this.mytodo = mytodo;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = mytodo.get(position);
        mytodo.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = mytodo.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getStatus());
        bundle.putString("task", item.getTask());

        AddNewTasks task = new AddNewTasks();
        task.setArguments(bundle);

        task.show(fragmentManager, task.getTag());
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
}
