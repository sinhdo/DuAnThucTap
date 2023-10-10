package com.example.duanthutap.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.MainActivity;
import com.example.duanthutap.R;
import com.example.duanthutap.activity.AddproductActivity;
import com.example.duanthutap.adapter.CartAdapter;
import com.example.duanthutap.adapter.ProductAdapter;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.ProductsAddCart;
import com.example.duanthutap.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements CartAdapter.Callback {
    Button btn_pay;
    TextView tv_total;
    private TextView taxPrice;
    private TextView totalPriceItem;
    private TextView totalPriceCart;
    RecyclerView recycler_listproductsadd;
    CartAdapter cartAdapter;
    private FirebaseUser firebaseUser;
    private int tax = 5;
    private ArrayList<ProductsAddCart> list = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetView(view);
        recycler_listproductsadd.setLayoutManager(new LinearLayoutManager(getActivity()));
        getListProductAddCart();
        cartAdapter = new CartAdapter(getActivity(), list, tv_total, this);
        recycler_listproductsadd.setAdapter(cartAdapter);

        sumPriceProduct();
    }

    private void GetView(View view) {
        btn_pay = view.findViewById(R.id.btn_pay);
        recycler_listproductsadd = view.findViewById(R.id.rcv_cart);
        taxPrice = (TextView) view.findViewById(R.id.tax_price);
        totalPriceItem = (TextView) view.findViewById(R.id.total_price_item);
        totalPriceCart = (TextView) view.findViewById(R.id.total_price_cart);
    }

    @Override
    public void deleteItemCart(ProductsAddCart products) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
        aBuilder.setTitle("Xóa sản phẩm trong giỏ hàng");
        aBuilder.setMessage("Bạn có chắc chắn muốn xóa " + products.getName_product() + " không?");
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String id_user = firebaseUser.getUid();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("cart").child(id_user).child(products.getId());
                myRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
                dialogInterface.dismiss();
            }
        });

        aBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                ;
            }
        });
        AlertDialog alertDialog = aBuilder.create();
        alertDialog.show();
    }

    private void getListProductAddCart() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("cart").child(id_user);
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductsAddCart product = dataSnapshot.getValue(ProductsAddCart.class);
                    list.add(product);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users faild", Toast.LENGTH_SHORT).show();
                Log.d("LISTCART", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void sumPriceProduct() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("cart").child(id_user);
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ProductsAddCart> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ProductsAddCart product = dataSnapshot.getValue(ProductsAddCart.class);
                    list.add(product);
                }

                if (list.size() == 0) {
                    return;
                } else {
                    double totalAllProduct = caculatorTotalPrice(list);
                    double totalCart = totalAllProduct + tax;
                    totalPriceItem.setText("$ " + totalAllProduct);
                    totalPriceCart.setText("$ " + totalCart);
                    taxPrice.setText("$ " + tax);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi", "onCancelled: " + error.getMessage());
            }
        });
    }

    private double caculatorTotalPrice(List<ProductsAddCart> productList) {
        double totalPrice = 0;
        for (ProductsAddCart product : productList) {
            totalPrice += product.getPricetotal_product() * product.getNum_product();
        }
        return totalPrice;
    }

    @Override
    public void onResume() {
        super.onResume();
        sumPriceProduct();
    }
}