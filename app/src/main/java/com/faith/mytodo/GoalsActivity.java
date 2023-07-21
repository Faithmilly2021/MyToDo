package com.faith.mytodo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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

public class GoalsActivity extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private GroupDoAdapter adapter;
    private List<ToDoModel> mList;
    private RecyclerView recyclerView;
    private List<ToDoModel> individualTasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        firestore = FirebaseFirestore.getInstance();

        //individualTasksList = new ArrayList<>();
        mList = new ArrayList<>();
        //adapter = new GroupDoAdapter(this, fragmentManager, mList, "Goals");

        adapter = new GroupDoAdapter(this, getSupportFragmentManager(), mList, "Goals", new CategoryClickListener() {
            @Override
            public void onCategoryClick(ToDoModel toDoModel) {
                // Handle the category click event here
                // For example, you can open the TaskActivity and pass necessary data
                // Intent intent = new Intent(GoalsActivity.this, TaskActivity.class);
                // intent.putExtra("categoryName", toDoModel.getCategoryName());
                // intent.putStringArrayListExtra("tasksListForCategory", (ArrayList<String>) toDoModel.getTasks());
                // startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        fetchGroupTasks();
    }

    private void fetchGroupTasks() {
        firestore.collection("groups")
                .document("Goals")
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
                                //ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withid(id);
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
