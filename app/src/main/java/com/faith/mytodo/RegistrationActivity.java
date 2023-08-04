package com.faith.mytodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.nullness.qual.NonNull;

public class RegistrationActivity extends AppCompatActivity {
    private EditText Rgemail, Rgpassword;
    private Button Rgbtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Rgemail = findViewById(R.id.Reg_email);
        Rgpassword = findViewById(R.id.Reg_password);
        Rgbtn = findViewById(R.id.Reg_btn);
        mAuth = FirebaseAuth.getInstance();

        Rgpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        Rgbtn.setOnClickListener(view -> {
            String email = Rgemail.getText().toString().trim();
            String password = Rgpassword.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User registration successful
                                FirebaseUser user = mAuth.getCurrentUser();

                                // Send user information to Firebase
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
                                String userId = user.getUid();
                                String name = ""; // Update this with the name value

                                // Create a user object with the necessary information
                                User newUser = new User(userId);

                                // Save the user object to the Firebase Realtime Database
                                databaseRef.child(userId).setValue(newUser);

                                // Proceed to the AddAccountActivity
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                finish(); // Optional: Close the registration activity
                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(RegistrationActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        });
    }
}
