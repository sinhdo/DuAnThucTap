package com.example.duanthutap;


import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import android.os.Bundle;

import android.widget.FrameLayout;
import com.example.duanthutap.databinding.ActivityMainBinding;

import com.example.duanthutap.fragment.CartFragment;
import com.example.duanthutap.fragment.CategoryFragment;
import com.example.duanthutap.fragment.HomeFragment;
import com.example.duanthutap.fragment.NotificationFragment;
import com.example.duanthutap.fragment.ProfileFragment;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment =new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_layout,homeFragment).commit();
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.category) {
                replaceFragment(new CategoryFragment());
            } else if (id == R.id.cart) {
                replaceFragment(new CartFragment());
            }else if(id==R.id.notification){
                replaceFragment(new NotificationFragment());
            } else if (id == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }else {
                replaceFragment(new HomeFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
        //thu
    }
}