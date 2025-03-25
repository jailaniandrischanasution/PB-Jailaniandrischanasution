package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView welcomeTextView;
    private TextView userNameTextView;
    private MaterialCardView financeCard, memoCard, taskCard, settingsCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        welcomeTextView = view.findViewById(R.id.welcomeTextView);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        financeCard = view.findViewById(R.id.financeCard);
        memoCard = view.findViewById(R.id.memoCard);
        taskCard = view.findViewById(R.id.taskCard);
        settingsCard = view.findViewById(R.id.settingsCard);

        // Fetch and display user's name
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("username").getValue(String.class);
                        userNameTextView.setText(username);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load username", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Set click listeners for cards
        financeCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Finance Tracker Coming Soon", Toast.LENGTH_SHORT).show();
        });

        memoCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Personal Memo Coming Soon", Toast.LENGTH_SHORT).show();
        });

        taskCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Task Management Coming Soon", Toast.LENGTH_SHORT).show();
        });

        settingsCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Settings Coming Soon", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}