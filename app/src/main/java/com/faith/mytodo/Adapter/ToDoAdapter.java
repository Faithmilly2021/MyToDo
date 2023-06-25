package com.faith.mytodo.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.checkerframework.checker.nullness.qual.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {
    private List<ToDoModel> mytodo;
    private Activity activity;
    private FirebaseFirestore firestore;
    private boolean displayDueDate;

    public ToDoAdapter(Activity activity, List<ToDoModel> mytodo, boolean displayDueDate) {
        this.activity = activity;
        this.mytodo = mytodo;
        this.displayDueDate = displayDueDate;
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
        ToDoModel toDoModel = mytodo.get(position);
        holder.mCheckbox.setText(toDoModel.getTask());
        holder.mCheckbox.setChecked(toBoolean(toDoModel.getStatus()));
    }

    private boolean toBoolean(int status) {
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return mytodo.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckbox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckbox = itemView.findViewById(R.id.mcheckBox);
        }
    }
}



