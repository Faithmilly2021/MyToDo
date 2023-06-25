package com.faith.mytodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.faith.mytodo.Adapter.ToDoAdapter;
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

public class MiscellaneousActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> mList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscellaneous);

        recyclerView = findViewById(R.id.recyclerView);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, 1, getResources().getColor(R.color.white));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firestore = FirebaseFirestore.getInstance();

        mList = new ArrayList<>();
        adapter = new ToDoAdapter(this, mList, false); // Set displayDueDate to false
        recyclerView.setAdapter(adapter);

        showData();
    }

    private void showData() {
        firestore.collection("groups").document("Miscellaneous").collection("tasks")
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
