package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.LocationAdapter;
import com.example.duanthutap.databinding.ActivityInfoLocationBinding;
import com.example.duanthutap.model.Location;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfoLocationActivity extends AppCompatActivity implements LocationAdapter.Callback, View.OnClickListener {
    private ActivityInfoLocationBinding binding;
    private FirebaseUser firebaseUser;
    private LocationAdapter adapter;
    private ArrayList<Location> list = new ArrayList<>();
    private LinearLayout lnl;
    private ImageButton imgBack;
    private EditText edName;
    private EditText edPhone;
    private EditText edLocation;
    private Button btnEditLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rcvAddressInfo.setLayoutManager(new LinearLayoutManager(this));
        showList();

        binding.imgBack.setOnClickListener(this);
        binding.imgAddAddress.setOnClickListener(this);
    }

    private void showList(){
        getListInfoLocation();
        adapter = new LocationAdapter(getApplicationContext(), list,this);
        binding.rcvAddressInfo.setAdapter(adapter);
    }
    private void getListInfoLocation(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("user").child(id_user).child("infoLocation");
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null){
                    list.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);
                    list.add(location);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("LISTCART", "onCancelled: " + error.getMessage());
            }
        });
    }

    @Override
    public void editAddress(Location address) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_address);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        if (dialog!= null&& dialog.getWindow()!= null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

        lnl = (LinearLayout) dialog.findViewById(R.id.lnl);
        imgBack = (ImageButton) dialog.findViewById(R.id.img_back);
        edName = (EditText) dialog.findViewById(R.id.ed_name);
        edPhone = (EditText) dialog.findViewById(R.id.ed_phone);
        edLocation = (EditText) dialog.findViewById(R.id.ed_location);
        btnEditLocation = (Button) dialog.findViewById(R.id.btn_editLocation);

        edName.setText(address.getName());
        edPhone.setText(address.getPhone());
        edLocation.setText(address.getLocation());

        btnEditLocation.setOnClickListener(view -> {
            String name = edName.getText().toString().trim();
            String phone = edPhone.getText().toString().trim();
            String location = edLocation.getText().toString().trim();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String id_user = firebaseUser.getUid();
            DatabaseReference myRef = firebaseDatabase.getReference("user").child(id_user).child("infoLocation");
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", name);
            updates.put("phone", phone);
            updates.put("location", location);
            myRef.child(address.getId()).updateChildren(updates);
            dialog.dismiss();
        });

        imgBack.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    @Override
    public void deleteAddress(Location address) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_user = firebaseUser.getUid();
        DatabaseReference myRef = firebaseDatabase.getReference("user").child(id_user).child("infoLocation");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này ?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {

            myRef.child(address.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle error here
                        Exception e = task.getException();
                        if (e != null) {
                            Log.d("Lỗi", "onComplete: "+e);
                        }
                    }
                }
            });
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back){
            finish();
        } else if (view.getId() == R.id.img_addAddress) {
            startActivity(new Intent(InfoLocationActivity.this, AddLocationActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }
}