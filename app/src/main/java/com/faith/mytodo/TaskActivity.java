package com.faith.mytodo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Adapter.ToDoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter toDoAdapter;
    private List<String> tasksList;
    private FloatingActionButton fab;
    private AlertDialog alertDialog;
    private List<String> newTasksList = new ArrayList<>();
    private Map<String, List<String>> categoryTasksMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.addTaskButtoN);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });

        String categoryName = getIntent().getStringExtra("categoryName");
        categoryTasksMap = (HashMap<String, List<String>>) getIntent().getSerializableExtra("categoryTasksMap");

        if (categoryTasksMap == null) {
            categoryTasksMap = new HashMap<>(); // Initialize the categoryTasksMap if it is null
        }

        tasksList = categoryTasksMap.get(categoryName);
        if (tasksList == null) {
            tasksList = new ArrayList<>(); // Initialize the tasksList if it is null
            categoryTasksMap.put(categoryName, tasksList); // Add the empty list to the map for the new category
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName);
        }

        toDoAdapter = new ToDoAdapter(this, tasksList);
        tasksRecyclerView.setAdapter(toDoAdapter);

        // Add a horizontal divider between items in the RecyclerView
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        tasksRecyclerView.addItemDecoration(itemDecoration);
    }

    private void showAddTaskDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.task_add_dialog, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add Task");

        // Initialize views in the dialog
        EditText etTaskDetails = dialogView.findViewById(R.id.etTask);
        Button btnAddTask = dialogView.findViewById(R.id.button_save);

        // Handle button click in the dialog
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskDetails = etTaskDetails.getText().toString().trim();
                if (!taskDetails.isEmpty()) {
                    // Add the new task to the newTasksList
                    newTasksList.add(taskDetails);

                    // Update the tasksList with the newTasksList
                    tasksList.addAll(newTasksList);

                    // Notify the adapter that the data has changed
                    toDoAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
