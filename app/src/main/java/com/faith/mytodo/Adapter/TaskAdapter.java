package com.faith.mytodo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.AddNewTasks;
import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<ToDoModel> mytodo;
    private String groupName;
    private FirebaseFirestore firestore;
    private AppCompatActivity activity;  // Change the activity type as needed
    private OnTaskCheckedChangeListener listener;

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(ToDoModel toDoModel, boolean isChecked);

        void onTaskCheckedChanged(String taskId, boolean isChecked);
    }

    public TaskAdapter(List<ToDoModel> mytodo, String groupName, AppCompatActivity activity) {
        this.mytodo = mytodo;
        this.groupName = groupName;
        this.activity = activity;
    }

    public void setOnTaskCheckedChangeListener(OnTaskCheckedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
        firestore = FirebaseFirestore.getInstance();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ToDoModel toDoModel = mytodo.get(position);
        holder.mcheckbox.setText(toDoModel.getTask());
        //holder.datebtn.setText(toDoModel.getDueDate()); // Update with your appropriate date field
        holder.staricon.setVisibility(View.VISIBLE);

        holder.mcheckbox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onTaskCheckedChanged(toDoModel, isChecked);
                }

                if (isChecked) {
                    firestore.collection("Individualtask")
                            .document(groupName)
                            .collection("tasks")
                            .document(toDoModel.getTask())
                            .update("status", 1);
                } else {
                    firestore.collection("Individualtask")
                            .document(groupName)
                            .collection("tasks")
                            .document(toDoModel.getTask())
                            .update("status", 0);
                }
            }
        });
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    public void setTasks(List<ToDoModel> mytodo){
        this.mytodo = mytodo;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModel item = mytodo.get(position);
        mytodo.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = mytodo.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getStatus());
        bundle.putString("task" , item.getTask());

        AddNewTasks task = new AddNewTasks();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());
    }


    @Override
    public int getItemCount() {
        return mytodo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //TextView datebtn, timebtn;
        CheckBox mcheckbox;
        ImageView staricon;

        public MyViewHolder(View itemView) {
            super(itemView);

           // datebtn = itemView.findViewById(R.id.due_date_tv);
            //timebtn = itemView.findViewById(R.id.due_time_tv);
            mcheckbox = itemView.findViewById(R.id.mcheckbox);
            staricon = itemView.findViewById(R.id.star_icon);
        }
    }
}
