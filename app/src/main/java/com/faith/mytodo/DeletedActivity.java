package com.faith.mytodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.faith.mytodo.Adapter.ToDoAdapter;
import com.faith.mytodo.Model.ToDoModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeletedActivity extends AppCompatActivity {
    private RecyclerView importantTasksRecyclerView;
    private ToDoAdapter importantTaskAdapter;
    private List<ToDoModel> importantTasksList;
    private FirebaseFirestore firestore;
    private TextView tasktextdisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted);

        // Check if the intent contains the "task" extra
        if (getIntent().hasExtra("task")) {
            ToDoModel task = getIntent().getParcelableExtra("task");


            TextView taskTitleTextView = findViewById(R.id.taskDetailsTextView);
            taskTitleTextView.setText(task.getTask());
        }

        importantTasksRecyclerView = findViewById(R.id.importantTasksRecyclerView);
        importantTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        importantTasksList = new ArrayList<>();
        //importantTaskAdapter = new ToDoAdapter(this, importantTasksList, true);
        importantTaskAdapter = new ToDoAdapter(this, importantTasksList, false, this, getSupportFragmentManager());

        importantTasksRecyclerView.setAdapter(importantTaskAdapter);
        androidx.recyclerview.widget.DividerItemDecoration itemDecoration = new androidx.recyclerview.widget.DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        importantTasksRecyclerView.addItemDecoration(itemDecoration);

        firestore = FirebaseFirestore.getInstance();
        fetchDeletedTasksFromFirestore();
    }


    private void fetchDeletedTasksFromFirestore() {
        CollectionReference tasksCollection = firestore.collection("tasks");

        tasksCollection.whereEqualTo("isDeleted", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle error
                            return;
                        }

                        importantTasksList.clear();
                        for (DocumentSnapshot document : snapshot.getDocuments()) {
                            ToDoModel task = document.toObject(ToDoModel.class).withId(document.getId());
                            importantTasksList.add(task);
                        }
                        importantTaskAdapter.notifyDataSetChanged();
                    }
                });
    }
}