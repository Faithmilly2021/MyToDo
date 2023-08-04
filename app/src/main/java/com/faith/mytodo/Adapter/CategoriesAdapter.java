package com.faith.mytodo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.R;
import com.faith.mytodo.TaskActivity;
import com.faith.mytodo.TaskGroup;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
    private List<TaskGroup> categoriesList;
    private Context context;

    public CategoriesAdapter(List<TaskGroup> categoriesList, Context context) {
        this.categoriesList = categoriesList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        TaskGroup category = categoriesList.get(position);
        holder.categoryNameTextView.setText(category.getName());
        // Set other category information as needed (e.g., category icon)
        // You can also add click listeners to the category item view to handle click events
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle category item click here, e.g., open TaskActivity
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("categoryName", category.getName());
                // You can pass any necessary data to the TaskActivity using intent.putExtra() if needed
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        ImageView categoryIcon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            categoryIcon = itemView.findViewById(R.id.categoryIconImageView);

            // Customize the appearance of the category name here
            categoryNameTextView.setTypeface(null, Typeface.BOLD);
            categoryNameTextView.setTextSize(20);
            categoryNameTextView.setTextColor(Color.WHITE);
        }
    }
}


