package com.example.duanthutap.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.ChatBoxActivity;
import com.example.duanthutap.adapter.ProductAdapter;
import com.example.duanthutap.adapter.SliderAdapter;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.ProductsAddCart;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ProductAdapter.Callback{
    private Timer timer;
    private ViewPager vpgSlideImage;
    private CircleIndicator circleIndicator;
    private RecyclerView rcvListitem;
    private ProductAdapter adapter;
    private Button selectButton = null;
    private MaterialButton selectColor = null;
    private int num;
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;
    List<Product> mList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vpgSlideImage = (ViewPager) view.findViewById(R.id.vpg_slide_image);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        rcvListitem = (RecyclerView) view.findViewById(R.id.rcv_listitem);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.banner1);
        imageList.add(R.drawable.banner2);
        imageList.add(R.drawable.banner3);
        imageList.add(R.drawable.banner4);


        SliderAdapter sliderAdapter = new SliderAdapter(getActivity(), imageList);
        vpgSlideImage.setAdapter(sliderAdapter);

        circleIndicator.setViewPager(vpgSlideImage);
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int currentItem = vpgSlideImage.getCurrentItem();
                            int totalItems = vpgSlideImage.getAdapter().getCount();
                            int nextItem = (currentItem + 1) % totalItems;
                            vpgSlideImage.setCurrentItem(nextItem);
                        }
                    });
                }
            }
        }, 2000, 2000);

        getListProduct();
        rcvListitem.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(getContext(),mList,this);
        rcvListitem.setAdapter(adapter);
        Log.d("ProductData1", "Product List: " + mList.toString());

//    private List<Product> limit4Product(){
//        getListProduct();
//        int maxProductsToShow = 4;
//        if (mList.size() > maxProductsToShow) {
//            mList = mList.subList(0, maxProductsToShow);
//            Log.d("hello", "onViewCreated: "+mList.size());
//        }
//        return mList;
    }

    private void getListProduct(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mList !=null){
                    mList.clear();
                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    mList.add(product);
                }
                Collections.sort(mList, new Comparator<Product>() {
                    @Override
                    public int compare(Product product1, Product product2) {
                        return product2.getSold() - product1.getSold();
                    }
                });

                int maxProductsToShow = 8;
                if (mList.size() > maxProductsToShow) {
                    List<Product> limitedList = mList.subList(0, maxProductsToShow);
                    adapter.setProductList(limitedList); // Đặt danh sách mới cho adapter
                } else {
                    adapter.setProductList(mList); // Sử dụng toàn bộ danh sách nếu ít hơn hoặc bằng 8 sản phẩm
                }

                Log.d("ProductData", "Product List: " + mList.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }

    @Override
    public void itemProductInfo(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.FullScreenDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_info_product, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        //final ViewPager[] imgFood = {dialog.findViewById(R.id.vpg_slide_product)};
        TextView tvName = (TextView) dialog.findViewById(R.id.tv_name_product);
        TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_priceProduct);
        ImageView imgMinus = (ImageView) dialog.findViewById(R.id.img_minus);
        ImageView  imgPlus = (ImageView) dialog.findViewById(R.id.img_plus);
        final ImageView[] imgPr = {dialog.findViewById(R.id.img_pr)};
        TextView tvNum = (TextView) dialog.findViewById(R.id.tv_num);
        MaterialButton btnBrown =  dialog.findViewById(R.id.btn_brown);
        MaterialButton btnWhite = dialog.findViewById(R.id.btn_white);
        MaterialButton  btnBlack = dialog.findViewById(R.id.btn_black);

        ImageButton imgback = dialog.findViewById(R.id.img_back);
        Button   btnXs = (Button) dialog.findViewById(R.id.btn_xs);
        Button btnS = (Button) dialog.findViewById(R.id.btn_s);
        Button  btnM = (Button) dialog.findViewById(R.id.btn_m);
        Button btnXl = (Button) dialog.findViewById(R.id.btn_xl);


        TextView tvTotalPrice = (TextView) dialog.findViewById(R.id.tvTotalPrice);
        TextView tvAddToCart = (TextView) dialog.findViewById(R.id.tvAddToCart);
        ImageView imgBackToCategory = (ImageView) dialog.findViewById(R.id.img_back);


        num =1;

        String imgUrl = product.getImg();
        Picasso.get().load(imgUrl).into(imgPr[0]);
        tvName.setText(product.getName());
        tvPrice.setText("$ "+product.getPrice());
        tvNum.setText(num+"");
        tvTotalPrice.setText("$ "+num*product.getPrice());

        imgBackToCategory.setOnClickListener(view -> {
            dialog.dismiss();
        });
        imgMinus.setOnClickListener(view -> {
            if (num > 1){
                num--;
                tvNum.setText(num+"");
                tvTotalPrice.setText("$ "+num*product.getPrice());
            }
        });
        imgPlus.setOnClickListener(view -> {
            if (num < product.getQuantity()){
                num++;
                tvNum.setText(num+"");
                tvTotalPrice.setText("$ "+num*product.getPrice());
            }
        });
        btnBrown.setOnClickListener(v->{
            if (selectColor == btnBrown) {
                btnBrown.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorNormal)));
                selectColor = null; // Đặt màu đã chọn về null
                product.setColor(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                // Nếu màu nâu không có viền đỏ, đặt viền đỏ cho màu nâu và lưu màu nâu là màu đã chọn
                if (selectColor != null) {
                    selectColor.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorNormal)));
                }
                btnBrown.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorRed)));
                selectColor = btnBrown;
                product.setColor("Nâu");
            }
        });
        btnWhite.setOnClickListener(v->{
            if (selectColor == btnWhite) {
                btnWhite.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorNormal)));
                selectColor = null; // Đặt màu đã chọn về null
                product.setColor(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                // Nếu màu nâu không có viền đỏ, đặt viền đỏ cho màu nâu và lưu màu nâu là màu đã chọn
                if (selectColor != null) {
                    selectColor.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorNormal)));
                }
                btnWhite.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorRed)));
                selectColor = btnWhite;
                product.setColor("Trắng");
            }

        });
        btnBlack.setOnClickListener(v->{
            if (selectColor == btnBlack) {
                btnBlack.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorNormal)));
                selectColor = null; // Đặt màu đã chọn về null
                product.setColor(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                // Nếu màu nâu không có viền đỏ, đặt viền đỏ cho màu nâu và lưu màu nâu là màu đã chọn
                if (selectColor != null) {
                    selectColor.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorNormal)));
                }
                btnBlack.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorRed)));
                selectColor = btnBlack;
                product.setColor("Đen");
            }
        });

        btnXs.setOnClickListener(v->{
            if (selectButton == btnXs) {
                selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                selectButton = null; // Đặt màu đã chọn về null
                product.setSize(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                if (selectButton != null) {
                    selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                btnXs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                selectButton = btnXs;
                product.setSize("Xs");
            }
        });
        btnS.setOnClickListener(v->{
            if (selectButton == btnS) {
                selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                selectButton = null; // Đặt màu đã chọn về null
                product.setSize(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                if (selectButton != null) {
                    selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                btnS.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                selectButton = btnS;
                product.setSize("S");
            }
        });
        btnM.setOnClickListener(v->{
            if (selectButton == btnM) {
                selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                selectButton = null; // Đặt màu đã chọn về null
                product.setSize(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                if (selectButton != null) {
                    selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                btnM.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                selectButton = btnM;
                product.setSize("M");
            }
        });
        btnXl.setOnClickListener(v->{
            if (selectButton == btnXl) {
                selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                selectButton = null; // Đặt màu đã chọn về null
                product.setSize(""); // Đặt giá trị màu của sản phẩm về rỗng hoặc giá trị mặc định
            } else {
                if (selectButton != null) {
                    selectButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                }
                btnXl.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                selectButton = btnXl;
                product.setSize("Xl");
            }
        });
        imgback.setOnClickListener(v->{
            dialog.dismiss();
        });
        tvAddToCart.setOnClickListener(v1->{
            String id_user = firebaseUser.getUid();
            String id_product = product.getId();
            String name = product.getName();
            String img = product.getImg();
            String color = product.getColor();
            String size = product.getSize();
            double price = product.getPrice();
            int num = Integer.parseInt(tvNum.getText().toString().trim());

            if ( color == null || size == null){
                Toast.makeText(getActivity(), "Lựa chọn đầy đủ thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            } else if (color.equals("") || size.equals("")) {
                Toast.makeText(getActivity(), "Lựa chọn đầy đủ thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            } else {
                mReference = FirebaseDatabase.getInstance().getReference().child("cart");
                String newKey = mReference.push().getKey();
                ProductsAddCart product1 = new ProductsAddCart(newKey, id_user, id_product, name,color,size,img, num, price);
                mReference.child(id_user).child(newKey).setValue(product1);
                Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}