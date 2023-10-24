package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.VoucherAdapter;
import com.example.duanthutap.databinding.ActivityVoucherBinding;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.ProductsAddCart;
import com.example.duanthutap.model.Voucher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    ActivityVoucherBinding binding;
    VoucherAdapter adapter;
    VoucherAdapter adapterFreeShip;
    List<Voucher> v;
    List<Voucher> vFreeShip;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoucherBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("voucher");

        v = new ArrayList<>();
        vFreeShip = new ArrayList<>();

        /*List<Voucher> v = getVoucher();

        for (int i = 0; i < v.size(); i++) {
            String newKey = myRef.push().getKey();
            v.get(i).setId(newKey);
            addVoucher(v.get(i));
        }*/

        getListVoucher("VOUCHER");
        getListVoucher("FREE_SHIP");

        adapter = new VoucherAdapter(v, new VoucherAdapter.onItemClick() {
            @Override
            public void onItemSelected(Voucher voucher) {

            }
        });
        binding.voucherMagiam.setAdapter(adapter);

        adapterFreeShip = new VoucherAdapter(vFreeShip, new VoucherAdapter.onItemClick() {
            @Override
            public void onItemSelected(Voucher voucher) {

            }
        });
        binding.voucherFreeship.setAdapter(adapterFreeShip);

    }
    private List<Voucher> getVoucher(){
        List<Voucher> magiam = new ArrayList<>();
        magiam.add(new Voucher("","VOUCHER","Voucher 10%","","","","","https://www.droppii.com/wp-content/uploads/2023/02/dropship-40.png", false));
        magiam.add(new Voucher("","VOUCHER","Voucher 20%","","","","","https://www.droppii.com/wp-content/uploads/2023/02/dropship-40.png", false));
        magiam.add(new Voucher("","VOUCHER","Voucher 50%","","","","","https://www.droppii.com/wp-content/uploads/2023/02/dropship-40.png", false));
        magiam.add(new Voucher("","VOUCHER","Voucher 100%","","","","","https://www.droppii.com/wp-content/uploads/2023/02/dropship-40.png", false));

        magiam.add(new Voucher("","FREE_SHIP","Voucher 10%","","","","","https://whisisvietnam.vn/wp-content/uploads/Freeship-100k.png", false));
        magiam.add(new Voucher("","FREE_SHIP","Voucher 20%","","","","","https://whisisvietnam.vn/wp-content/uploads/Freeship-100k.png", false));
        magiam.add(new Voucher("","FREE_SHIP","Voucher 50%","","","","","https://whisisvietnam.vn/wp-content/uploads/Freeship-100k.png", false));
        magiam.add(new Voucher("","FREE_SHIP","Voucher 100%","","","","","https://whisisvietnam.vn/wp-content/uploads/Freeship-100k.png", false));
        return magiam;
    }
    private void getListVoucher(String type) {
        myRef.orderByChild("type").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Voucher voucher;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    voucher = dataSnapshot.getValue(Voucher.class);
                    if (type.equals("FREE_SHIP")){
                        vFreeShip.add(voucher);
                    }else {
                        v.add(voucher);
                    }
                }

                if (type.equals("FREE_SHIP")){
                    adapterFreeShip.notifyDataSetChanged();
                }else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VoucherActivity.this, "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addVoucher(Voucher voucher){
        String id = voucher.getId();
        myRef.child(id).setValue(voucher, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.e("TAG", "onComplete: ");
                } else {
                    Toast.makeText(VoucherActivity.this, "Lỗi khi lưu sản phẩm vào Realtime Database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}