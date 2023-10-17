package com.example.duanthutap.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.MainActivity;
import com.example.duanthutap.R;
import com.example.duanthutap.activity.AddproductActivity;
import com.example.duanthutap.activity.ShowListLocationActivity;
import com.example.duanthutap.adapter.CartAdapter;
import com.example.duanthutap.adapter.ProductAdapter;
import com.example.duanthutap.model.Location;
import com.example.duanthutap.model.Oder;
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
import java.util.Date;
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
    private TextView tvPayment;
    private LinearLayout lnlShowLocation;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvLocation;
    private TextView tvShowLocation;
    RecyclerView recycler_listproductsadd;
    CartAdapter cartAdapter;
    private FirebaseUser firebaseUser;
    private RelativeLayout rlAddress;
    private ProductsAddCart product;
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
        poppuGetListPayment();
        sumPriceProduct();
        setLocation();
        rlAddress.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), ShowListLocationActivity.class));
        });
        btn_pay.setOnClickListener(view1 -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference("list_oder");
            String newKey = myRef.push().getKey();
            String id_user = firebaseUser.getUid();
            String name = product.getName_product();
            String image = product.getImage_product();
            String total = totalPriceCart.getText().toString();
            String date = new Date().toString();
            String address = tvLocation.getText().toString();
            String phone_number = tvPhone.getText().toString();
            String status = "delivery";
            Oder oder = new Oder(newKey,id_user,name,image,Double.parseDouble(total),date,address,phone_number,status);
            On_Create_Bill(oder);
        });
    }
    private void On_Create_Bill(Oder oder) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_oder");
        String id = oder.getId();

        myRef.child(id).setValue(oder, (error, ref) -> {
           if (error == null){
               Toast.makeText(getContext(), "Thành công add bill", Toast.LENGTH_SHORT).show();
           }else {
               // Xảy ra lỗi khi lưu sản phẩm
               Toast.makeText(getContext(), "Lỗi khi lưu sản phẩm vào Realtime Database", Toast.LENGTH_SHORT).show();
           }
        });
    }

    private void GetView(View view) {
        btn_pay = view.findViewById(R.id.btn_pay);
        recycler_listproductsadd = view.findViewById(R.id.rcv_cart);
        taxPrice = (TextView) view.findViewById(R.id.tax_price);
        totalPriceItem = (TextView) view.findViewById(R.id.total_price_item);
        totalPriceCart = (TextView) view.findViewById(R.id.total_price_cart);
        tvPayment = (TextView) view.findViewById(R.id.tv_payment);
        rlAddress = (RelativeLayout) view.findViewById(R.id.rl_address);
        lnlShowLocation = (LinearLayout) view.findViewById(R.id.lnl_showLocation);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        tvShowLocation = (TextView) view.findViewById(R.id.tv_showLocation);
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
                    product = dataSnapshot.getValue(ProductsAddCart.class);
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
                    totalPriceCart.setText(String.valueOf(totalCart));
                    taxPrice.setText("$ " + tax);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void poppuGetListPayment(){
        String [] listPayment ={"Thanh toán khi nhận hàng","Thanh toán qua thẻ ngân hàng"};
        tvPayment.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), tvPayment);
            for (String address : listPayment) {
                popupMenu.getMenu().add(address);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String selectedAddress = item.getTitle().toString();
                    tvPayment.setText(selectedAddress);
                    // Xử lý khi chọn địa chỉ từ danh sách
                    return true;
                }
            });
            popupMenu.show();
        });
    }
    private double caculatorTotalPrice(List<ProductsAddCart> productList) {
        double totalPrice = 0;
        for (ProductsAddCart product : productList) {
            totalPrice += product.getPricetotal_product() * product.getNum_product();
        }
        return totalPrice;
    }

    private void setLocation(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PrefLocation", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        String location = sharedPreferences.getString("location", "");

        if (name.isEmpty()){
            lnlShowLocation.setVisibility(View.GONE);
            tvShowLocation.setVisibility(View.VISIBLE);
        } else {
            lnlShowLocation.setVisibility(View.VISIBLE);
            tvShowLocation.setVisibility(View.GONE);
            tvName.setText(name);
            tvPhone.setText(phone);
            tvLocation.setText(location);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sumPriceProduct();
        setLocation();
    }
}