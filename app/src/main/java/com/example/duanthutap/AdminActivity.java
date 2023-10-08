package com.example.duanthutap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.duanthutap.Order.OrderActivity;
import com.example.duanthutap.databinding.ActivityAdminBinding;
import com.example.duanthutap.databinding.ActivityMainBinding;
import com.example.duanthutap.fragment.CartFragment;
import com.example.duanthutap.fragment.CategoryFragment;
import com.example.duanthutap.fragment.HomeFragment;
import com.example.duanthutap.fragment.NotificationFragment;
import com.example.duanthutap.fragment.ProfileFragment;

public class AdminActivity extends AppCompatActivity {
    ActivityAdminBinding binding;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();
        HomeFragment homeFragment =new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_layout,homeFragment).commit();
        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.ql_product) {
                replaceFragment(new CategoryFragment());
            }else if(id==R.id.notification){
                replaceFragment(new NotificationFragment());
            } else if (id == R.id.ql_user) {
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