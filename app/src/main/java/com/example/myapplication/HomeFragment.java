package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.Calendar;

public class HomeFragment extends Fragment {

    private TextView greetingTextView;
    private TextView userNameTextView;
    private EditText searchEditText;
    private ImageView profileImageView;
    private MaterialCardView financeCard, memoCard, taskCard, settingsCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        greetingTextView = view.findViewById(R.id.greetingTextView);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        searchEditText = view.findViewById(R.id.searchEditText);
        profileImageView = view.findViewById(R.id.profileImageView);
        financeCard = view.findViewById(R.id.financeCard);
        memoCard = view.findViewById(R.id.memoCard);
        taskCard = view.findViewById(R.id.taskCard);
        settingsCard = view.findViewById(R.id.settingsCard);

        // Set time-based greeting
        setGreeting();

        // Set search functionality
        setupSearch();

        // Fetch and display user's name
        fetchUserData();

        // Set click listeners for cards
        setupCardListeners();

        return view;
    }

    private void setGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greeting = "Good morning,";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = "Good afternoon,";
        } else {
            greeting = "Good evening,";
        }

        greetingTextView.setText(greeting);
    }

    private void setupSearch() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Toast.makeText(getContext(), "Searching: " + v.getText(), Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void fetchUserData() {
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
    }

    private void setupCardListeners() {
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
    }
}