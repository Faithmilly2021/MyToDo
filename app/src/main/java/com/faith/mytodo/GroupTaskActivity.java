package com.faith.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GroupTaskActivity extends AppCompatActivity {

    private EditText taskDescriptionEditText;
    private Button dueDateButton;
    private Button dueTimeButton;
    private Button saveButton;
    private String groupName;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_task);

        taskDescriptionEditText = findViewById(R.id.duetaskDescription);
        dueDateButton = findViewById(R.id.dueDateButton);
        dueTimeButton = findViewById(R.id.dueTimeButton);
        saveButton = findViewById(R.id.dueSavebutton);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm a", Locale.US);

        groupName = getIntent().getStringExtra("groupName");

        firestore = FirebaseFirestore.getInstance();

        // Retrieve task information from AddNewTask dialog
        Intent intent = getIntent();
        String taskDescription = intent.getStringExtra("taskDescription");
        String dueDate = intent.getStringExtra("dueDate");
        String dueTime = intent.getStringExtra("dueTime");

        // Set the retrieved task information
        taskDescriptionEditText.setText(taskDescription);
        dueDateButton.setText(dueDate);
        dueTimeButton.setText(dueTime);

        // Hide due date and time buttons for "Personal" and "Miscellaneous" groups
        if (groupName.equals("Personal") || groupName.equals("Miscellaneous")) {
            dueDateButton.setVisibility(View.GONE);
            dueTimeButton.setVisibility(View.GONE);
        }

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        dueTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

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
                        // Include due date and time for other groups
                        taskMap.put("group", groupName);
                        taskMap.put("due", dueDateButton.getText().toString());
                        taskMap.put("status", 0);
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


        private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDueDateButton();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                GroupTaskActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDueTimeButton();
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                GroupTaskActivity.this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false);
        timePickerDialog.show();
    }

    private void updateDueDateButton() {
        dueDateButton.setText(dateFormatter.format(calendar.getTime()));
    }

    private void updateDueTimeButton() {
        dueTimeButton.setText(timeFormatter.format(calendar.getTime()));
    }
}


