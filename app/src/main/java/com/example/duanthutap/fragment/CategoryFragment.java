package com.example.duanthutap.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.AddproductActivity;
import com.example.duanthutap.adapter.ProductAdapter;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.ProductsAddCart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private List<Product> mList = new ArrayList<>();
    List<Product> searchResultsList = new ArrayList<>();
    private ImageButton selectedImageButton = null;
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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        imgAddProduct.setOnClickListener(v->{
            startActivity(new Intent(getContext(), AddproductActivity.class));
        });

        rcvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(null,mList,this);
        rcvProduct.setAdapter(adapter);
        getListProduct();
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
        setRoleListUser(product);
    }

    public void setRoleListUser(Product product){
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isAdmin = dataSnapshot.child("role").getValue(Boolean.class);
                    // Xử lý tùy theo giá trị Boolean (true/false)
                    if (isAdmin) {
                        // Người dùng là Admin
                        dialogEditProduct(product);
                    } else {
                        dialogProduct(product);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void dialogProduct(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.FullScreenDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_product, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final ImageView[] imgFood = {(ImageView) dialog.findViewById(R.id.imgFood)};
        TextView tvName = (TextView) dialog.findViewById(R.id.tv_name);
        TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_price);
        ImageView  imgMinus = (ImageView) dialog.findViewById(R.id.img_minus);
        ImageView  imgPlus = (ImageView) dialog.findViewById(R.id.img_plus);
        TextView  tvDes = (TextView) dialog.findViewById(R.id.tv_des);
        TextView tvNum = (TextView) dialog.findViewById(R.id.tv_num);
        TextView  tvQuantity = (TextView) dialog.findViewById(R.id.tv_quantity);
        TextView tvTotalPrice = (TextView) dialog.findViewById(R.id.tvTotalPrice);
        TextView tvAddToCart = (TextView) dialog.findViewById(R.id.tvAddToCart);

        num =1;

        // Ban đầu, tính và hiển thị tổng giá tiền
        // String imgUrl = adapter.getItem(holder.getAdapterPosition()).getImg();
        String imgUrl = product.getImg();
        Picasso.get().load(imgUrl).into(imgFood[0]);
        tvName.setText(product.getName());
        tvPrice.setText("$ "+product.getPrice());
        tvDes.setText(product.getDescription());
        tvQuantity.setText("Kho: "+ product.getQuantity());
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
        tvAddToCart.setOnClickListener(v1->{
            String id_user = firebaseUser.getUid();
            String id_product = product.getId();
            String name = product.getName();
            String img = product.getImg();
            double price = product.getPrice();
            int num = Integer.parseInt(tvNum.getText().toString().trim());


            mReference = FirebaseDatabase.getInstance().getReference().child("cart");
            String newKey = mReference.push().getKey();
            ProductsAddCart product1 = new ProductsAddCart(newKey, id_user, id_product, name, img, num, price);
            mReference.child(id_user).child(newKey).setValue(product1);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layoutMain, new CartFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.cart);
            dialog.dismiss();
        });
    }

    private void dialogEditProduct(Product product){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.FullScreenDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final ImageView[] imgFood = {(ImageView) dialog.findViewById(R.id.imgFood)};
        EditText edtName = (EditText) dialog.findViewById(R.id.edtName);
        EditText edtColor = (EditText) dialog.findViewById(R.id.edtColor);
        EditText edtQuantity = (EditText) dialog.findViewById(R.id.edtQuantity);
        EditText edtPrice = (EditText) dialog.findViewById(R.id.edtPrice);
        EditText edtCategory = (EditText) dialog.findViewById(R.id.edtCategory);

        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
        Button btnDelete = (Button) dialog.findViewById(R.id.btnDelete);

        Picasso.get().load(product.getImg()).into(imgFood[0]);
        edtName.setText(product.getName());
        edtColor.setText(product.getColor());
        edtQuantity.setText(""+product.getQuantity());
        edtPrice.setText(product.getPrice().toString());
        edtCategory.setText(product.getCategory());

        btnUpdate.setOnClickListener(view -> {
            product.setName(edtName.getText().toString());
            product.setColor(edtColor.getText().toString());
            product.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
            product.setPrice(Double.parseDouble(edtPrice.getText().toString()));
            product.setCategory(edtCategory.getText().toString());

            updateProduct(product);

            dialog.dismiss();
        });

        btnDelete.setOnClickListener(view -> {
            deleteProduct(product.getId());
            dialog.dismiss();
        });
    }

    private void deleteProduct(String id){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");
        myRef.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi lưu sản phẩm
                    Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateProduct(Product product){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");

        String id = product.getId();
        myRef.child(id).setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getContext(), "Update sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi lưu sản phẩm
                    Toast.makeText(getContext(), "Update thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}