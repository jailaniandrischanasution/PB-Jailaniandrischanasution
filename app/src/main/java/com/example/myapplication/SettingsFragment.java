package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private Switch notificationSwitch;
    private Switch darkModeSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize switches
        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        // Notification switch listener
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Implement notification settings logic
            Toast.makeText(getContext(),
                    isChecked ? "Notifications Enabled" : "Notifications Disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Dark mode switch listener
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Implement dark mode toggle logic
            Toast.makeText(getContext(),
                    isChecked ? "Dark Mode Enabled" : "Dark Mode Disabled",
                    Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}