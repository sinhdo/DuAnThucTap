package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.duanthutap.R;
import com.example.duanthutap.database.FirebaseHelper;
import com.example.duanthutap.databinding.ActivityInfoUserBinding;
import com.example.duanthutap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class InfoUserActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityInfoUserBinding binding;
    private DatabaseReference mDatabaseReference;
    private Picasso picasso = Picasso.get();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        binding.imgEdit.setOnClickListener(this);
        binding.imgBacktoprofile.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String email = sharedPreferences.getString("email", "");
        String img = sharedPreferences.getString("img", "");
        String phone = sharedPreferences.getString("phone", "");
        String address = sharedPreferences.getString("address", "");

        if (name.isEmpty() || phone.isEmpty() || !binding.tvPhone.getText().equals(phone)){
            setInfo();
        } else {
            binding.tvName.setText(name);
            binding.tvEmail.setText(email);
            binding.tvPhone.setText(phone);
            binding.tvLocation.setText(address);
            if (img.equals("")){
                binding.imgUser.setImageResource(R.drawable.ic_google);
            } else {
                picasso.load(img).into(binding.imgUser);
            }
        }
    }

    private void setInfo(){
        String id = firebaseUser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(id);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String img = snapshot.child("img").getValue(String.class);

                binding.tvName.setText(name);
                binding.tvEmail.setText(email);
                binding.tvPhone.setText(phone);
                binding.tvLocation.setText(address);

                if (img.equals("")) {
                    binding.imgUser.setImageResource(R.drawable.ic_google);
                } else {
                    picasso.load(img).into(binding.imgUser);
                }
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("img", img);
                editor.putString("phone", phone);
                editor.putString("address", address);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi", "onCancelled: " + error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_edit) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            binding.imgEdit.setVisibility(View.INVISIBLE);
            binding.imgBacktoprofile.setVisibility(View.INVISIBLE);
            binding.lnlInfoUser.setVisibility(View.INVISIBLE);

            binding.lnlUpdateUser.setVisibility(View.VISIBLE);
            binding.imgCamera.setVisibility(View.VISIBLE);

            String id = firebaseUser.getUid();
            mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(id);
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phoneNumber").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String img = snapshot.child("img").getValue(String.class);

                    binding.edName.setText(name);
                    binding.edPhone.setText(phone);
                    binding.edLocation.setText(address);
                    binding.edImg.setText(img);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Loi", "onCancelled: " + error.getMessage());
                }
            });

            binding.btnUpdateUser.setOnClickListener(view1 -> {
                String name = binding.edName.getText().toString().trim();
                String phone = binding.edPhone.getText().toString().trim();
                String address = binding.edLocation.getText().toString().trim();
                String img = binding.edImg.getText().toString().trim();

                Map<String, Object> updates = new HashMap<>();
                updates.put("name", name);
                updates.put("img", img);
                updates.put("address", address);
                updates.put("phoneNumber", phone);

                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(id);
                mDatabaseReference.updateChildren(updates);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("img", img);
                editor.putString("phone", phone);
                editor.putString("address", address);
                editor.apply();

                binding.imgEdit.setVisibility(View.VISIBLE);
                binding.imgBacktoprofile.setVisibility(View.VISIBLE);
                binding.lnlInfoUser.setVisibility(View.VISIBLE);

                setInfo();

                binding.lnlUpdateUser.setVisibility(View.INVISIBLE);
                binding.imgCamera.setVisibility(View.INVISIBLE);
            });

            binding.btnCancle.setOnClickListener(view1 -> {
                binding.imgEdit.setVisibility(View.VISIBLE);
                binding.imgBacktoprofile.setVisibility(View.VISIBLE);
                binding.lnlInfoUser.setVisibility(View.VISIBLE);

                binding.lnlUpdateUser.setVisibility(View.INVISIBLE);
                binding.imgCamera.setVisibility(View.INVISIBLE);
            });
        } else if (view.getId() == R.id.img_backtoprofile) {
            finish();
        }
    }
}