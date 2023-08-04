package com.faith.mytodo;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faith.mytodo.Adapter.CategoriesAdapter;
import com.faith.mytodo.GroupAdapter;
import com.faith.mytodo.TaskGroup;
import com.faith.mytodo.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener, DialogInterface.OnDismissListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener, GroupAdapter.OnCategoryClickListener {

    private static final String CHANNEL_ID = "ToDoListChannel";
    private static final int NOTIFICATION_ID = 1;

    private List<TaskGroup> groupsList;
    private GroupAdapter groupAdapter;
    private TextView notificationCount;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private RecyclerView recyclerView, recyclerView1;
    private SearchView searchView;
    private FloatingActionButton fab;
    private FirebaseFirestore firestore;
    private CategoriesAdapter categoriesAdapter;
    private FirebaseAuth firebaseAuth;
    private List<TaskGroup> categoriesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView1 = findViewById(R.id.caterecyclerView);
        notificationCount = findViewById(R.id.notification_count);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.close_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#192A56")));

        navigationView.setNavigationItemSelectedListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        groupAdapter = new GroupAdapter(new ArrayList<>(), this, this);
        recyclerView.setAdapter(groupAdapter);

        // Initialize categoriesList with categories from SharedPreferences
        categoriesList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> categoriesSet = sharedPreferences.getStringSet("categories", new HashSet<>());
        Log.d("Categories", categoriesSet.toString());
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoriesList, this);
        recyclerView1.setAdapter(categoriesAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        groupsList = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

       if (currentUser != null) {
            String userId = currentUser.getUid();
            groupsList.add(new TaskGroup("Personal", "groupId1", "Personal", Arrays.asList("Task1", "Task2"), null, R.drawable.ic_account, true, userId, false));
            groupsList.add(new TaskGroup("Goals", "groupId3", "Goals", Arrays.asList("Task5", "Task6"), null, R.drawable.ic_editcalendar, true, userId, false));
            groupsList.add(new TaskGroup("Miscellaneous", "groupId4", "Miscellaneous", Arrays.asList("Task1", "Task2"), null, R.drawable.ic_list, true, userId, false));
            groupsList.add(new TaskGroup("Private", "groupId2", "Private", Arrays.asList("Task3", "Task4"), null, R.drawable.ic_privacy, true, userId, false));

        } else {
            // Handling the case where the user is not authenticated
        }
        groupAdapter = new GroupAdapter(groupsList, this, this);
        recyclerView.setAdapter(groupAdapter);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_important:
                        startActivity(new Intent(MainActivity.this, ImportantActivity.class));
                        return true;
                    case R.id.nav_delete:
                        startActivity(new Intent(MainActivity.this, DeletedActivity.class));
                        return true;
                }
                return false;
            }
        });
    }


    private void showAddCategoryDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_category);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final EditText categoryEditText = dialog.findViewById(R.id.category_edit_text);
        Button addButton = dialog.findViewById(R.id.add_button);
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoriesList, this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName = categoryEditText.getText().toString().trim();
                if (!categoryName.isEmpty()) {
                    List<String> tasksListForCategory = new ArrayList<>();

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        TaskGroup newGroup = new TaskGroup(categoryName, "groupId", categoryName, tasksListForCategory, Collections.singletonList(R.drawable.ic_lists), 0, true, userId, true);

                        categoriesList.add(newGroup);
                        categoriesAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Category added: " + categoryName, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        // Save the category in Firestore
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        firestore.collection("categories").add(newGroup)
                                .addOnSuccessListener(documentReference -> {
                                    // Successfully added to Firestore
                                    Toast.makeText(MainActivity.this, "Category saved in Firestore", Toast.LENGTH_SHORT).show();

                                    // Save the category in SharedPreferences
                                    saveCategoryToSharedPreferences(categoryName);
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to add to Firestore
                                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                        intent.putExtra("categoryName", categoryName);
                        startActivity(intent);
                    } else {

                        Toast.makeText(MainActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a category name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();
    }


private void saveCategoryToSharedPreferences(String categoryName) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> categoriesSet = sharedPreferences.getStringSet("categories", new HashSet<>());
        categoriesSet.add(categoryName);
        sharedPreferences.edit().putStringSet("categories", categoriesSet).apply();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.nav_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.nav_notifications) {
            showNotifications("Notification Title", "Notification Message");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotifications(String title, String message) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.notification_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView titleTextView = dialog.findViewById(R.id.notification_title);
        TextView messageTextView = dialog.findViewById(R.id.notification_message);
        titleTextView.setText(title);
        messageTextView.setText(message);

        ImageView closeButton = dialog.findViewById(R.id.notification_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showReminderNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Task Reminder")
                .setContentText("Your task is due soon, don't forget!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Handle query text submission
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Handle query text change
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.nav_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.nav_share:
                shareApp();
                return true;
            case R.id.nav_logout:
                finishAffinity();
                Toast.makeText(MainActivity.this, "You have logged out. Goodbye!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Download the ToDoList app and manage your tasks efficiently!";
        String shareSubject = "ToDoList App";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        startActivity(Intent.createChooser(shareIntent, "Share using"));
    }

    private void updateNotificationCount() {
        int notificationCountValue = 0;

        if (notificationCountValue > 0) {
            notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText(String.valueOf(notificationCountValue));
        } else {
            notificationCount.setVisibility(View.VISIBLE);
        }
    }


    public void onItemClick(int position) {
        TaskGroup group = groupsList.get(position);
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("groupName", group.getName());
        intent.putExtra("groupsList", (ArrayList<TaskGroup>) groupsList);
        startActivity(intent);
    }


    @Override
    public void onCategoryClick(TaskGroup taskGroup) {
        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
        intent.putExtra("CategoryName", taskGroup.getName());
        startActivity(intent);
    }

}
