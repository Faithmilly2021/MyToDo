package com.faith.mytodo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<TaskGroup> groupsList;
    private OnItemClickListener listener;
    private OnCategoryClickListener categoryClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(TaskGroup taskGroup);
    }

    public GroupAdapter(List<TaskGroup> groupsList, OnItemClickListener listener, OnCategoryClickListener categoryClickListener) {
        this.groupsList = groupsList;
        this.listener = listener;
        this.categoryClickListener = categoryClickListener;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView groupIcon;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupIcon = itemView.findViewById(R.id.groupIcon);
        }

        public void bind(final TaskGroup taskGroup, final OnItemClickListener listener, final OnCategoryClickListener categoryClickListener, Context context) {
            if (taskGroup == null) {
                return;
            }

            groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (taskGroup.isCategory()) {
                        categoryClickListener.onCategoryClick(taskGroup);
                    } else {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            groupName.setTextColor(context.getResources().getColor(R.color.white));
            groupName.setTextAppearance(context, R.style.TextAppearance_AppCompat_Title);
            groupName.setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.drawable_padding));

            int iconResourceId = taskGroup.getIconResourceId();
            Drawable leftDrawable;

            if (iconResourceId != 0) {
                leftDrawable = ContextCompat.getDrawable(context, iconResourceId);
            } else {
                leftDrawable = ContextCompat.getDrawable(context, R.drawable.ic_groupicon); // Default drawable
            }

            groupName.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

            groupName.setPadding(0, 50, 0, 0);
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
        holder.bind(taskGroup, listener, categoryClickListener, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }
}
