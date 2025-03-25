package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Models.UserDetails;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView profileNameText, profileEmailText, profileNIMText;
    private LinearLayout editProfileOption, changePasswordOption;
    private MaterialButton logoutButton;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize views
        profileNameText = view.findViewById(R.id.profileNameText);
        profileEmailText = view.findViewById(R.id.profileEmailText);
        profileNIMText = view.findViewById(R.id.profileNIMText);
        editProfileOption = view.findViewById(R.id.editProfileOption);
        changePasswordOption = view.findViewById(R.id.changePasswordOption);
        logoutButton = view.findViewById(R.id.logoutButton);

        // Load user data
        loadUserData();

        // Set click listeners
        editProfileOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Edit Profile Coming Soon", Toast.LENGTH_SHORT).show();
        });

        changePasswordOption.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Change Password Coming Soon", Toast.LENGTH_SHORT).show();
        });

        logoutButton.setOnClickListener(v -> {
            firebaseAuth.signOut();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Navigate to login activity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserData() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Get user details from Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(currentUser.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);

                        if (userDetails != null) {
                            // Display username
                            if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
                                profileNameText.setText(userDetails.getUsername());
                            } else {
                                profileNameText.setText("Not available");
                            }

                            // Display email
                            if (userDetails.getUserEmail() != null && !userDetails.getUserEmail().isEmpty()) {
                                profileEmailText.setText(userDetails.getUserEmail());
                            } else if (currentUser.getEmail() != null) {
                                // Fallback to Firebase Auth email if not found in database
                                profileEmailText.setText(currentUser.getEmail());
                            } else {
                                profileEmailText.setText("Not available");
                            }

                            // Display NIM (Student ID)
                            if (userDetails.getUserNIM() != null && !userDetails.getUserNIM().isEmpty()) {
                                profileNIMText.setText(userDetails.getUserNIM());
                            } else {
                                profileNIMText.setText("Not available");
                            }
                        } else {
                            // If userDetails is null, try to get individual fields
                            String username = dataSnapshot.child("username").getValue(String.class);
                            String email = dataSnapshot.child("userEmail").getValue(String.class);
                            String nim = dataSnapshot.child("userNIM").getValue(String.class);

                            // Set values with fallbacks
                            profileNameText.setText(username != null ? username : "Not available");
                            profileEmailText.setText(email != null ? email :
                                    (currentUser.getEmail() != null ? currentUser.getEmail() : "Not available"));
                            profileNIMText.setText(nim != null ? nim : "Not available");
                        }
                    } else {
                        // Use data from Firebase Auth as fallback
                        profileEmailText.setText(currentUser.getEmail());
                        profileNameText.setText(currentUser.getDisplayName() != null ?
                                currentUser.getDisplayName() : "Not available");
                        profileNIMText.setText("Not available");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User not logged in, redirect to login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}