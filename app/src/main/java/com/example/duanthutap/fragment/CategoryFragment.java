package com.example.duanthutap.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.AddproductActivity;
import com.example.duanthutap.adapter.ProductAdapter;
import com.example.duanthutap.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
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

        imgAddProduct.setOnClickListener(v->{
            startActivity(new Intent(getContext(), AddproductActivity.class));
        });

        rcvProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(null,mList);
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

}