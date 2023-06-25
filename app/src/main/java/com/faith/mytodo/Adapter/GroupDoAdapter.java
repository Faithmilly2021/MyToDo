package com.faith.mytodo.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.checkerframework.checker.nullness.qual.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GroupDoAdapter extends RecyclerView.Adapter<GroupDoAdapter.MyViewHolder> {
    private List<ToDoModel> mytodo;
    private Activity activity;
    private FirebaseFirestore firestore;
    private String groupName;

    public GroupDoAdapter(Activity activity, List<ToDoModel> mytodo, String groupName) {
        this.mytodo = mytodo;
        this.activity = activity;
        this.groupName = groupName;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.group_task_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ToDoModel toDoModel = mytodo.get(position);
        holder.mcheckbox.setText(toDoModel.getTask()); // Set the task text
        holder.mduedatetv.setText(activity.getString(R.string.due_date_format, toDoModel.getDueDate()));
        holder.mstaricon.setVisibility(View.VISIBLE); // Show the star icon

        holder.mcheckbox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firestore.collection("groups")
                            .document(groupName)
                            .collection("tasks")
                            .document(toDoModel.getTask())
                            .update("status", 1);
                } else {
                    firestore.collection("groups")
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

    @Override
    public int getItemCount() {
        return mytodo.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mduedatetv;
        CheckBox mcheckbox;
        ImageView mstaricon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mduedatetv = itemView.findViewById(R.id.due_date_tv);
            mcheckbox = itemView.findViewById(R.id.mcheckbox);
            mstaricon = itemView.findViewById(R.id.star_icon);
        }
    }
}
