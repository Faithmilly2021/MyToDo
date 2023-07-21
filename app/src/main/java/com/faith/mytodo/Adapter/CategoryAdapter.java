package com.faith.mytodo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.GroupAdapter;
import com.faith.mytodo.R;
import com.faith.mytodo.TaskGroup;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<TaskGroup> categoriesList;
    private GroupAdapter.OnCategoryClickListener categoryClickListener;

    public CategoryAdapter(List<TaskGroup> categoriesList, GroupAdapter.OnCategoryClickListener categoryClickListener) {
        this.categoriesList = categoriesList;
        this.categoryClickListener = categoryClickListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        TaskGroup taskGroup = categoriesList.get(position);
        holder.bind(taskGroup);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView groupName;
        public ImageView groupIcon;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            groupIcon = itemView.findViewById(R.id.groupIcon);
        }

        public void bind(final TaskGroup taskGroup) {
            groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event for categories
                    categoryClickListener.onCategoryClick(taskGroup);
                }
            });

            // Set the data to the views in the item layout
            groupName.setText(taskGroup.getName());
            groupIcon.setImageResource(taskGroup.getIconResourceId());
        }
    }
}

