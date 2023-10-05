package com.example.duanthutap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.duanthutap.databinding.ActivityMainBinding;
import com.example.duanthutap.fragment.CartFragment;
import com.example.duanthutap.fragment.HomeFragment;
import com.example.duanthutap.fragment.NotificationFragment;
import com.example.duanthutap.fragment.ProfileFragment;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }
}