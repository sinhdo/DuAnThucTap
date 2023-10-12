package com.example.duanthutap.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.duanthutap.R;
import com.example.duanthutap.databinding.ActivityAddLocationBinding;
import com.example.duanthutap.model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLocationActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddLocationBinding binding;
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(this);
        binding.btnAddLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back){
            finish();
        } else if (view.getId() == R.id.btn_addLocation) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String id_user = firebaseUser.getUid();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user").child(id_user).child("infoLocation");
            String name = binding.edName.getText().toString().trim();
            String phone = binding.edPhone.getText().toString().trim();
            String location = binding.edLocation.getText().toString().trim();
            String newKey = myRef.push().getKey();
            Location location1 = new Location(newKey,name,phone,location);
            myRef.child(newKey).setValue(location1);
            finish();
        }
    }
}