package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.duanthutap.AdminActivity;
import com.example.duanthutap.MainActivity;
import com.example.duanthutap.R;
import com.example.duanthutap.database.FirebaseRole;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManHinhChaoActivity extends AppCompatActivity {
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chao);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }

    private void nextActivity() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            startActivity(new Intent(ManHinhChaoActivity.this, LoginActivity.class));
        } else {
            setRoleAdd();
        }
    }
    public void setRoleAdd() {
        String id = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = FirebaseRole.isUserAdmin(dataSnapshot);
                // Xử lý tùy theo giá trị Boolean (true/false)
                if (isAdmin) {
                    // Người dùng là Admin
                    startActivity(new Intent(ManHinhChaoActivity.this, AdminActivity.class));
                } else {
                    // Người dùng không phải là Admin
                    startActivity(new Intent(ManHinhChaoActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}