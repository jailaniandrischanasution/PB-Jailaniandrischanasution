package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Models.UserDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button signUpBtn;
    TextInputEditText usernameSignUp, passwordSingUp, nimPengguna, emailPengguna;
    FirebaseAuth mAuth;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        signUpBtn = findViewById(R.id.signUpBtn);
        usernameSignUp = findViewById(R.id.usernameSignUp);
        emailPengguna = findViewById(R.id.emailPengguna);
        passwordSingUp = findViewById(R.id.passwordSingUp);
        nimPengguna = findViewById(R.id.nimPengguna);

        signUpBtn.setOnClickListener(view -> {
            String username = String.valueOf(usernameSignUp.getText()).trim();
            String email = String.valueOf(emailPengguna.getText()).trim();
            String password = String.valueOf(passwordSingUp.getText()).trim();
            String NIM = String.valueOf(nimPengguna.getText()).trim();

            // Validate input
            if (TextUtils.isEmpty(username)) {
                usernameSignUp.setError("Username is required");
                usernameSignUp.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                emailPengguna.setError("Email is required");
                emailPengguna.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordSingUp.setError("Password is required");
                passwordSingUp.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordSingUp.setError("Password must be at least 6 characters");
                passwordSingUp.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(NIM)) {
                nimPengguna.setError("NIM is required");
                nimPengguna.requestFocus();
                return;
            }

            // Register user
            registerUser(username, email, password, NIM);
        });
    }

    private void registerUser(String username, String email, String password, String NIM) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Get current user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            // Create UserDetails object
                            UserDetails userDetails = new UserDetails(uid, username, email, password, NIM);

                            // Save user details to Realtime Database
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                            reference.child(uid).setValue(userDetails)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // Send email verification
                                            firebaseUser.sendEmailVerification()
                                                    .addOnCompleteListener(verificationTask -> {
                                                        if (verificationTask.isSuccessful()) {
                                                            Toast.makeText(RegisterActivity.this,
                                                                    "Registration successful. Please check your email for verification.",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                            // Navigate to Home Activity
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this,
                                                    "Failed to save user details",
                                                    Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "User details save failed", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Registration failed", task.getException());
                    }
                });
    }
}