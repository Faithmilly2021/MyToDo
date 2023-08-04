package com.faith.mytodo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryDialogFragment extends DialogFragment {
    private FirebaseFirestore firestore;
    private OnDialogCloseListener onDialogCloseListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_category, null);
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        firestore = FirebaseFirestore.getInstance();

        // Find views and set click listeners
        final EditText categoryEditText = dialog.findViewById(R.id.category_edit_text);
        Button addButton = dialog.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryEditText.getText().toString().trim();
                if (!categoryName.isEmpty()) {
                    Map<String, Object> categoryMap = new HashMap<>();
                    categoryMap.put("category", categoryName);
                    firestore.collection("categories").add(categoryMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Category Saved", Toast.LENGTH_SHORT).show();
                                dismiss();
                                if (onDialogCloseListener != null) {
                                    onDialogCloseListener.onDialogClose((DialogInterface) AddCategoryDialogFragment.this);
                                }
                            } else {
                                Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Please enter a category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return dialog;
    }

    // Method to set the listener to notify the parent activity when the dialog is closed
    public void setOnDialogCloseListener(OnDialogCloseListener listener) {
        this.onDialogCloseListener = listener;
    }
}


