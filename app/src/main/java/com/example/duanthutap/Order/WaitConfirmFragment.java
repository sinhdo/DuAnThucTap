package com.example.duanthutap.Order;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.OderAdapter;
import com.example.duanthutap.model.Oder;
import com.example.duanthutap.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaitConfirmFragment extends Fragment implements OderAdapter.Callback {

    public WaitConfirmFragment() {
    }

    public static WaitConfirmFragment newInstance() {
        WaitConfirmFragment fragment = new WaitConfirmFragment();
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
        return inflater.inflate(R.layout.fragment_wait_confirm, container, false);
    }

    private RecyclerView rcvConfirm;
    private OderAdapter oderAdapter;
    private ArrayList<Oder> list = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvConfirm = view.findViewById(R.id.rcv_confirm);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CheckRoleUser();
        rcvConfirm.setLayoutManager(new LinearLayoutManager(getContext()));
        oderAdapter = new OderAdapter(getContext(), list, this);
        rcvConfirm.setAdapter(oderAdapter);
    }

    private void GetAllData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference("list_oder");
        myReference.orderByChild("status").equalTo("pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Oder oder = dataSnapshot.getValue(Oder.class);
                    list.add(oder);
                }
                oderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users failed", Toast.LENGTH_SHORT).show();
                Log.d("LIST-CART", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void GetDataConfirmList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("list_oder");

        myReference.orderByChild("status").equalTo("pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Oder oder = dataSnapshot.getValue(Oder.class);
                    if (oder.getId_user().equals(id_user)) {
                        list.add(oder);
                    }
                }
                oderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users failed", Toast.LENGTH_SHORT).show();
                Log.d("LIST-CART", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void CheckRoleUser() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isAdmin = Boolean.TRUE.equals(dataSnapshot.child("role").getValue(Boolean.class));
                    if (isAdmin) {
                        GetAllData();
                    } else {
                        GetDataConfirmList();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", "OnCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void CheckRoleUserFunction(Oder oder) {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isAdmin = Boolean.TRUE.equals(dataSnapshot.child("role").getValue(Boolean.class));
                    if (isAdmin) {
                        dialogForAdmin(oder);
                    } else {
                        dialogForUser(oder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", "OnCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void dialogForUser(Oder oder) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_cancel);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.bg_dialog));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnExit = dialog.findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(view -> {
            dialog.cancel();
        });
        btnCancel.setOnClickListener(view -> {
            oder.setStatus("canceled");
            UpdateStatus(oder);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void dialogForAdmin(Oder oder) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.bg_dialog));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        windowAttributes.gravity = Gravity.BOTTOM;
        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm);
        Button btnExit = dialog.findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(view -> {
            dialog.cancel();
        });
        btnConfirm.setOnClickListener(view -> {
            oder.setStatus("delivery");
            GetNameProduct(oder.getId());
            UpdateStatus(oder);

            dialog.dismiss();
        });
        dialog.show();
    }

    private void UpdateStatus(Oder oder) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_oder");
        String id = oder.getId();
        myRef.child(id).setValue(oder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getContext(), "Update status", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Update fall", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void logic(Oder oder) {
        CheckRoleUserFunction(oder);
    }

    private void GetNameProduct(String abc) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("list_oder");

        orderRef.orderByChild(abc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderId = orderSnapshot.child("id").getValue(String.class);
                    if (orderId.equals(abc)) {
                        String id_user = orderSnapshot.child("id_user").getValue(String.class);
                        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(id_user);
                        cartRef.orderByChild("id_user").equalTo(id_user).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                                        String id_cart = cartSnapshot.getKey();
                                        String id_Product = cartSnapshot.child("id_product").getValue(String.class);
                                        int num_product = cartSnapshot.child("num_product").getValue(Integer.class);
                                        Log.d("======", "num_product: "+num_product+"id pro "+id_Product);
                                        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("list_product");
                                        productRef.orderByChild("id").equalTo(id_Product).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot Prosnapshot) {
                                                if (Prosnapshot.exists()) {
                                                  for (DataSnapshot productSanpshot: Prosnapshot.getChildren()){
                                                      DatabaseReference protRef = FirebaseDatabase.getInstance().getReference("list_product").child(id_Product);
                                                      Product product = productSanpshot.getValue(Product.class);
                                                      if (product!=null){
                                                          int sold = product.getSold();
                                                          int quantity = product.getQuantity();
                                                          String name = product.getName();
                                                          Log.d("========", "sold: " + sold + " quan " + quantity+" name "+name);
                                                    product.setSold(sold + num_product);
                                                    product.setQuantity(quantity - num_product);
                                                          protRef.child("sold").setValue(product.getSold());
                                                          protRef.child("quantity").setValue(product.getQuantity());
                                                      }else {
                                                          Log.d("======", "onDataChange: Product NUll");
                                                      }

                                                  }

                                                } else {
                                                    Log.d("=====", "Không vào: " + Prosnapshot);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Không tìm thấy giỏ hàng", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d("======", "onCancelled: " + error.getMessage());
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("=====", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}