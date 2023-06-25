package com.faith.mytodo;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import com.faith.mytodo.Adapter.ToDoAdapter;
import com.faith.mytodo.Model.ToDoModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;




public class MainActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener, DialogInterface.OnDismissListener, SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String CHANNEL_ID = "ToDoListChannel";
    private static final int NOTIFICATION_ID = 1;

    private List<TaskGroup> groupsList;
    private GroupAdapter groupAdapter;
    private TextView notificationCount;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemBackgroundResource(R.color.dark_blue);


        // layout params of the ConstraintLayout
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) bottomNavigationView.getLayoutParams();

        // the constraints for the BottomNavigationView
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        bottomNavigationView.setLayoutParams(layoutParams);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#192A56")));

        notificationCount = findViewById(R.id.notification_count);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        recyclerView = findViewById(R.id.recyclerView);



        // Set up ActionBarDrawerToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_Open, R.string.close_menu);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up NavigationView
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        groupsList = new ArrayList<>();
       groupsList.add(new TaskGroup("Hey! Don't forget to group your task accordingly", "", "", Arrays.asList("Task1", "Task2"), Arrays.asList(R.color.groupColor1), true, R.drawable.ic_groupicon));
        groupsList.add(new TaskGroup("Personal", "groupId1", "Personal", Arrays.asList("Task1", "Task2"), Arrays.asList(R.color.groupColor1), false, R.drawable.ic_account));
        groupsList.add(new TaskGroup("Work", "groupId2", "Work", Arrays.asList("Task3", "Task4"), Arrays.asList(R.color.groupColor2), true, R.drawable.ic_work));
        groupsList.add(new TaskGroup("Goals", "groupId3", "Goals", Arrays.asList("Task5", "Task6"), Arrays.asList(R.color.groupColor3), true, R.drawable.ic_editcalendar));
        groupsList.add(new TaskGroup("Miscellaneous", "groupId4", "Miscellaneous", Arrays.asList("Task7", "Task8"), Arrays.asList(R.color.groupColor4), false, R.drawable.ic_list));
        groupsList.add(new TaskGroup("Private", "groupId5", "Private", Arrays.asList("Task9", "Task10"), Arrays.asList(R.color.groupColor5), true, R.drawable.ic_privacy));

        groupAdapter = new GroupAdapter(groupsList, this);
        recyclerView.setAdapter(groupAdapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_personal:
                        startActivity(new Intent(MainActivity.this, PersonalActivity.class));
                        return true;
                    case R.id.nav_work:
                        startActivity(new Intent(MainActivity.this, WorkActivity.class));
                        return true;
                    case R.id.nav_goals:
                        startActivity(new Intent(MainActivity.this, GoalsActivity.class));
                        return true;
                    case R.id.nav_miscellaneous:
                        startActivity(new Intent(MainActivity.this, MiscellaneousActivity.class));
                        return true;
                    case R.id.nav_private:
                        startActivity(new Intent(MainActivity.this, PrivateActivity.class));
                        return true;

                }
                return false;
            }
        });

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
        // Handle dialog dismiss event
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
            case R.id.nav_recyclebin:
                startActivity(new Intent(MainActivity.this, RecyclebinActivity.class));
                return true;
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
            notificationCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(int position) {
        TaskGroup group = groupsList.get(position);
        Intent intent = new Intent(MainActivity.this, GroupTaskActivity.class);
        intent.putExtra("groupName", group.getName());
        intent.putExtra("groupsList", (ArrayList<TaskGroup>) groupsList);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
