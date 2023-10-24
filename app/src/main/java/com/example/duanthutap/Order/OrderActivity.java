package com.example.duanthutap.Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.ViewPagerAdapter;
import com.example.duanthutap.databinding.ActivityOrderBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityOrderBinding binding;
    private ViewPagerAdapter adapter;
    private ImageButton imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ViewPagerAdapter(this);
        binding.vpgBill.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.idTablayout, binding.vpgBill, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Chờ xác nhận");
                        break;
                    case 1:
                        tab.setText("Đang giao");
                        break;
                    case 2:
                        tab.setText("Đã thanh toán");
                        break;
                }
            }
        }); 
        tabLayoutMediator.attach();
        binding.imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back){
            finish();
        }
    }
}