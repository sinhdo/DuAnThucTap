package com.example.duanthutap.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.SliderAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private ImageButton imgBack;
    private ImageButton btnCart;
    private ViewPager vpgSlideProduct;
    private TextView tvNameProduct;
    private TextView tvPriceProduct;
    private TextView tvColor;
    private TextView tvSize;
    private Button btnAddToCart;
    private CircleIndicator circleIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

            FindView();
            List<Integer> imageList = new ArrayList<>();
            imageList.add(R.drawable.t_shirt);
            imageList.add(R.drawable.t_shirt);
            imageList.add(R.drawable.t_shirt);
            imageList.add(R.drawable.t_shirt);

            SliderAdapter sliderAdapter = new SliderAdapter(getApplicationContext(), imageList);
            vpgSlideProduct.setAdapter(sliderAdapter);
            vpgSlideProduct.setBackgroundResource(R.drawable.bg_search_view);

            circleIndicator.setViewPager(vpgSlideProduct);
            sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

    }
    private void FindView() {
        imgBack = (ImageButton) findViewById(R.id.img_back);
        btnCart = (ImageButton) findViewById(R.id.btn_cart);
        vpgSlideProduct = (ViewPager) findViewById(R.id.vpg_slide_product);
        tvNameProduct = (TextView) findViewById(R.id.tv_name_product);
        tvPriceProduct = (TextView) findViewById(R.id.tv_priceProduct);
        tvColor = (TextView) findViewById(R.id.tv_color);
        tvSize = (TextView) findViewById(R.id.tv_size);
        btnAddToCart = (Button) findViewById(R.id.btn_add_to_cart);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
    }
}