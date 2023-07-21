package com.faith.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class GroupTaskActivity extends AppCompatActivity {

    private EditText taskDescriptionEditText;
    private Button saveButton;
    private String groupName;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task);

        saveButton = findViewById(R.id.dueSavebutton);
        taskDescriptionEditText = findViewById(R.id.duetaskDescription);
        saveButton = findViewById(R.id.dueSavebutton);

        groupName = getIntent().getStringExtra("groupName");

        firestore = FirebaseFirestore.getInstance();

        // Retrieve task information from AddNewTask dialog
        Intent intent = getIntent();
        String taskDescription = intent.getStringExtra("taskDescription");

        // Set the retrieved task information
        taskDescriptionEditText.setText(taskDescription);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskDescriptionEditText.getText().toString();

                if (task.isEmpty()) {
                    Toast.makeText(GroupTaskActivity.this, "Empty task is not allowed!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> taskMap = new HashMap<>();
                    taskMap.put("task", task);

                    if (!(groupName.equals("Personal") || groupName.equals("Miscellaneous"))) {
                        // Include group information for other groups
                        taskMap.put("group", groupName);
                        taskMap.put("status", 0);
                        taskMap.put("time", FieldValue.serverTimestamp());
                    }

                    firestore.collection("groups").document(groupName).collection("tasks")
                            .add(taskMap)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(GroupTaskActivity.this, "Task Saved", Toast.LENGTH_SHORT).show();

                                        // Start the corresponding activity based on the group
                                        Intent intent;
                                        if (groupName.equals("Private")) {
                                            intent = new Intent(GroupTaskActivity.this, PrivateActivity.class);
                                        } else if (groupName.equals("Personal")) {
                                            intent = new Intent(GroupTaskActivity.this, PersonalActivity.class);
                                        } else if (groupName.equals("Goals")) {
                                            intent = new Intent(GroupTaskActivity.this, GoalsActivity.class);
                                        } else if (groupName.equals("Work")) {
                                            intent = new Intent(GroupTaskActivity.this, WorkActivity.class);
                                        } else if (groupName.equals("Miscellaneous")) {
                                            intent = new Intent(GroupTaskActivity.this, MiscellaneousActivity.class);
                                        } else {
                                            // Default to MainActivity if group name is unknown
                                            intent = new Intent(GroupTaskActivity.this, MainActivity.class);
                                        }
                                        startActivity(intent);

                                        finish(); // Finish the current activity to prevent going back to it
                                    } else {
                                        Toast.makeText(GroupTaskActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GroupTaskActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}


