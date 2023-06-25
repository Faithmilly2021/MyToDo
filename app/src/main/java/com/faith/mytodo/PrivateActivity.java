package com.faith.mytodo;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Adapter.GroupDoAdapter;
import com.faith.mytodo.Model.ToDoModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrivateActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private GroupDoAdapter adapter;
    private List<ToDoModel> mList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        firestore = FirebaseFirestore.getInstance();

        mList = new ArrayList<>();
       // adapter = new GroupDoAdapter(this, mList);
        adapter = new GroupDoAdapter(this, mList, "Private");
        recyclerView.setAdapter(adapter);

        fetchWorkTasks();
    }

    private void fetchWorkTasks() {
        firestore.collection("groups")
                .document("Private")
                .collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle error
                            return;
                        }

                        mList.clear();

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                String id = documentChange.getDocument().getId();
                                ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);
                                mList.add(toDoModel);
                            }
                        }

                        // Reverse the list to display the latest tasks first
                        Collections.reverse(mList);

                        adapter.notifyDataSetChanged();
                    }
                });
    }
}


