package com.faith.mytodo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AddNewTasks extends BottomSheetDialogFragment {

    public static final String TAG = "AddNewTask";

    private EditText taskDes;
    private TextView saveBtn;
    private FirebaseFirestore firestore;
    private Context context;

    public static AddNewTasks newInstance() {
        return new AddNewTasks();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_new_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskDes = view.findViewById(R.id.taskDescription);
        saveBtn = view.findViewById(R.id.Savebutton);

        firestore = FirebaseFirestore.getInstance();

        taskDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    saveBtn.setEnabled(false);
                    saveBtn.setBackgroundColor(Color.GRAY);
                } else {
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(getResources().getColor(R.color.green_blue));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = taskDes.getText().toString();

                if (task.isEmpty()) {
                    Toast.makeText(context, "Empty task is not allowed!", Toast.LENGTH_SHORT).show();
                } else {
                    saveTask(task);
                }
                dismiss();
            }
        });
    }

    public void saveTask(String task) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("task", task);
        taskMap.put("status", 0);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }

    public class OnSaveClickListener {
    }
}
