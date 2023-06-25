package com.faith.mytodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<TaskGroup> groupsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public GroupAdapter(List<TaskGroup> groupsList, OnItemClickListener listener) {
        this.groupsList = groupsList;
        this.listener = listener;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView groupIcon;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupIcon = itemView.findViewById(R.id.groupIcon);
        }

        public void bind(final TaskGroup taskGroup, final OnItemClickListener listener, Context context) {
            if (taskGroup.getName().equals("Hey! Don't forget to group your tasks accordingly")) {
                groupName.setClickable(false);
                groupName.setOnClickListener(null);
                groupName.setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1);
                groupName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                groupName.setPadding(0, context.getResources().getDimensionPixelSize(R.dimen.drawable_padding), 0, 0);
            } else {
                groupName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(getAdapterPosition());
                    }
                });
                groupName.setTextColor(context.getResources().getColor(R.color.white));
                groupName.setTextAppearance(context, R.style.TextAppearance_AppCompat_Title);
                groupName.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.drawable_padding));
                groupName.setCompoundDrawablesWithIntrinsicBounds(taskGroup.getIconResourceId(), 0, 0, 0);
                groupName.setPadding(0, 50, 0, 0);
            }

            groupName.setText(taskGroup.getName());

            groupIcon.setImageResource(taskGroup.getIconResourceId());
            groupIcon.setPadding(
                    context.getResources().getDimensionPixelSize(R.dimen.drawable_padding),
                    0,
                    context.getResources().getDimensionPixelSize(R.dimen.drawable_padding),
                    0);
        }
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        TaskGroup taskGroup = groupsList.get(position);
        holder.bind(taskGroup, listener, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }
}


