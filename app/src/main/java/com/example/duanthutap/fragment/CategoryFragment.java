package com.example.duanthutap.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.AddproductActivity;
import com.example.duanthutap.adapter.ProductAdapter;
import com.example.duanthutap.database.FirebaseRole;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.ProductsAddCart;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements ProductAdapter.Callback {
    private ImageView imgAddProduct;
    private ImageButton imgAll;
    private ImageButton imgPants;
    private ImageButton imgShirt;
    private ImageButton imgDress;
    private RecyclerView rcvProduct;
    private ProductAdapter adapter;
    List<Product> mList = new ArrayList<>();
    private SearchView searchView;
    private ImageButton selectedImageButton = null;
    private Button selectButton = null;
    private MaterialButton selectColor = null;
    private int num;
    private FirebaseUser firebaseUser;
    private DatabaseReference mReference;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgAddProduct = (ImageView) view.findViewById(R.id.img_addProduct);
        imgAll = (ImageButton) view.findViewById(R.id.img_all);
        imgPants = (ImageButton) view.findViewById(R.id.img_pants);
        imgShirt = (ImageButton) view.findViewById(R.id.img_shirt);
        imgDress = (ImageButton) view.findViewById(R.id.img_dress);
        rcvProduct = (RecyclerView) view.findViewById(R.id.rcv_product);
        searchView = view.findViewById(R.id.search_Pr);

        imgAddProduct.setOnClickListener(v->{
            setRoleAdd();
            startActivity(new Intent(getContext(), AddproductActivity.class));
        });
        rcvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(getContext(),mList,this);
        rcvProduct.setAdapter(adapter);
        getListProduct();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //s = searchView.getQuery().toString();
                performSearch (s);
                return true;
            }
        });

        imgAll.setOnClickListener(v->{
            if (selectedImageButton != null) {
                selectedImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }

            imgAll.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPressed));
            selectedImageButton = imgAll;
            getListProduct();
        });
        imgPants.setOnClickListener(v->{
            if (selectedImageButton != null) {
                selectedImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }

            imgPants.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPressed));
            selectedImageButton = imgPants;
            getListPants();
        });
        imgShirt.setOnClickListener(v->{
            if (selectedImageButton != null) {
                selectedImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }

            imgShirt.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPressed));
            selectedImageButton = imgShirt;
            getListShirt();
        });
        imgDress.setOnClickListener(v->{
            if (selectedImageButton != null) {
                selectedImageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorNormal));
            }

            imgDress.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPressed));
            selectedImageButton = imgDress;
            getListDress();
        });
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getListPants(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");

        myRef.orderByChild("category").equalTo("quần").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mList !=null){
                    mList.clear();
                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    mList.add(product);
                }
                adapter.setProductList(mList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getListShirt(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");

        myRef.orderByChild("category").equalTo("áo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mList !=null){
                    mList.clear();
                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    mList.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getListDress(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");

        myRef.orderByChild("category").equalTo("váy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mList !=null){
                    mList.clear();
                }
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    mList.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users faild", Toast.LENGTH_SHORT).show();
            }
        });
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
        ImageView  imgMinus = (ImageView) dialog.findViewById(R.id.img_minus);
        ImageView  imgPlus = (ImageView) dialog.findViewById(R.id.img_plus);
        final ImageView[] imgPr = {dialog.findViewById(R.id.img_pr)};
        TextView tvNum = (TextView) dialog.findViewById(R.id.tv_num);
        MaterialButton  btnBrown =  dialog.findViewById(R.id.btn_brown);
        MaterialButton btnWhite = dialog.findViewById(R.id.btn_white);
        MaterialButton  btnBlack = dialog.findViewById(R.id.btn_black);

        ImageButton imgback = dialog.findViewById(R.id.img_back);
         Button   btnXs = (Button) dialog.findViewById(R.id.btn_xs);
         Button btnS = (Button) dialog.findViewById(R.id.btn_s);
         Button  btnM = (Button) dialog.findViewById(R.id.btn_m);
         Button btnXl = (Button) dialog.findViewById(R.id.btn_xl);


        TextView tvTotalPrice = (TextView) dialog.findViewById(R.id.tvTotalPrice);
        TextView tvAddToCart = (TextView) dialog.findViewById(R.id.tvAddToCart);


        num =1;

        // Ban đầu, tính và hiển thị tổng giá tiền
        // String imgUrl = adapter.getItem(holder.getAdapterPosition()).getImg();
        String imgUrl = product.getImg();
        Picasso.get().load(imgUrl).into(imgPr[0]);
        tvName.setText(product.getName());
        tvPrice.setText("$ "+product.getPrice());
        tvNum.setText(num+"");
        tvTotalPrice.setText("$ "+num*product.getPrice());

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
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String id_user = firebaseUser.getUid();
            String id_product = product.getId();
            String name = product.getName();
            String img = product.getImg();
            String color = product.getColor();
            String size = product.getSize();
            double price = product.getPrice();
            int num = Integer.parseInt(tvNum.getText().toString().trim());


            mReference = FirebaseDatabase.getInstance().getReference().child("cart");
            String newKey = mReference.push().getKey();
            ProductsAddCart product1 = new ProductsAddCart(newKey, id_user, id_product, name,color,size,img, num, price);
            mReference.child(id_user).child(newKey).setValue(product1);

//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.frame_layoutMain, new CartFragment());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//
//            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
//            bottomNavigationView.setSelectedItemId(R.id.cart);
            Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }
    public void setRoleAdd() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = FirebaseRole.isUserAdmin(dataSnapshot);
                // Xử lý tùy theo giá trị Boolean (true/false)
                if (isAdmin) {
                    // Người dùng là Admin
                    imgAddProduct.setVisibility(View.VISIBLE);
                } else {
                    // Người dùng không phải là Admin
                    imgAddProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
    private void performSearch(String query) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");
        Query searchQuery = myRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff");

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        mList.add(product); // Thêm kết quả tìm kiếm vào danh sách
                    }
                }
                adapter.setProductList(mList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loi", "onCancelled: "+databaseError.getMessage());
            }
        });
    }
}