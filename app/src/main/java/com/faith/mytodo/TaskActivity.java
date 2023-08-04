package com.faith.mytodo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Adapter.ToDoAdapter;
import com.faith.mytodo.Model.ToDoModel;
import com.faith.mytodo.TouchHelper.TouchHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter toDoAdapter;
    private List<ToDoModel> tasksList;
    private FloatingActionButton fab;
    private String groupName;
    private String categoryName;
    private CollectionReference tasksCollection;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize tasksList before creating adapter
        tasksList = new ArrayList<>();

        toDoAdapter = new ToDoAdapter(this, tasksList, false, this, getSupportFragmentManager());
        tasksRecyclerView.setAdapter(toDoAdapter);

        // Initialize RecyclerView touch helper
        TouchHelper touchHelper = new TouchHelper(toDoAdapter);
        new ItemTouchHelper(touchHelper).attachToRecyclerView(tasksRecyclerView);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        Drawable drawable = getResources().getDrawable(R.drawable.white_divider);
        if (drawable != null) {
            itemDecoration.setDrawable(drawable);
        }
        tasksRecyclerView.addItemDecoration(itemDecoration);

        addDividerLines();

        groupName = getIntent().getStringExtra("groupName");
        categoryName = getIntent().getStringExtra("categoryName");
        if (groupName == null && categoryName == null) {
            Log.e("TaskActivity", "Group or category not specified.");
            finish();
            return;
        }

        firestore = FirebaseFirestore.getInstance();
        if (groupName != null) {
            tasksCollection = firestore.collection("groups").document(groupName).collection("tasks");
        } else if (categoryName != null) {
            tasksCollection = firestore.collection("categories").document(categoryName).collection("tasks");
        }

        fetchTasksFromFirestore();

        fab = findViewById(R.id.addTaskButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });

    }

    private void fetchTasksFromFirestore() {
        tasksCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TaskActivity", "Error fetching tasks: " + error.getMessage());
                    finish();
                    return;
                }

                tasksList.clear(); // Clear the list before adding new data
                for (DocumentSnapshot document : snapshot.getDocuments()) {
                    ToDoModel task = document.toObject(ToDoModel.class).withId(document.getId());
                    tasksList.add(task);
                }

                Collections.reverse(tasksList);
                toDoAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addDividerLines() {
        RelativeLayout layout = findViewById(R.id.rootLayout);
        int numDividers = 10; // Change this value as needed

        for (int i = 0; i < numDividers; i++) {
            View divider = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width
                    1 // Height (1dp)
            );
            divider.setLayoutParams(params);
            divider.setBackgroundColor(getResources().getColor(R.color.white));
            layout.addView(divider);
        }
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.task_add_dialog, null);
        builder.setView(dialogView);

        final EditText taskDescriptionEditText = dialogView.findViewById(R.id.etTask);
        Button saveButton = dialogView.findViewById(R.id.button_save);

        final AlertDialog dialog = builder.create();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskDescription = taskDescriptionEditText.getText().toString().trim();

                if (taskDescription.isEmpty()) {
                    Toast.makeText(TaskActivity.this, "Empty task is not allowed!", Toast.LENGTH_SHORT).show();
                } else {
                    addToFirestore(taskDescription);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void addToFirestore(String taskDescription) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("task", taskDescription);
        taskMap.put("status", 0);
        taskMap.put("important", false);
        taskMap.put("userId", userId);

        if (groupName != null) {
            taskMap.put("group", groupName);
        } else if (categoryName != null) {
            taskMap.put("category", categoryName);
        }

        tasksCollection.add(taskMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TaskActivity.this, "Task Saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TaskActivity.this, "Error saving task: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TaskActivity.this, "Error adding task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
