package com.faith.mytodo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> tasksList;
    private Context context;

    public ToDoAdapter(Context context, List<ToDoModel> tasksList) {
        this.context = context;
        this.tasksList = tasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ToDoModel task = tasksList.get(position);
        holder.mCheckbox.setText(task.getTask());
    }

    @Override
    public int getItemCount() {
        return tasksList != null ? tasksList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCheckbox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}

