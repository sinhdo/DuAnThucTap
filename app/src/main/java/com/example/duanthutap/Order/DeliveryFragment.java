package com.example.duanthutap.Order;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.duanthutap.model.ProductsAddCart;
import com.example.duanthutap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryFragment extends Fragment implements OderAdapter.Callback {

    public DeliveryFragment() {
        // Required empty public constructor
    }

    public static DeliveryFragment newInstance() {
        DeliveryFragment fragment = new DeliveryFragment();
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
        return inflater.inflate(R.layout.fragment_delivery, container, false);
    }

    private RecyclerView rcvDelivery;
    private OderAdapter oderAdapter;
    private ArrayList<Oder> list = new ArrayList<>();
    private FirebaseUser firebaseUser;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvDelivery = (RecyclerView) view.findViewById(R.id.rcv_delivery);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        CheckRoleUser();
        rcvDelivery.setLayoutManager(new LinearLayoutManager(getContext()));
        oderAdapter = new OderAdapter(getContext(), list, this);
        rcvDelivery.setAdapter(oderAdapter);

    }

    public void CheckRoleUser() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isAdmin = dataSnapshot.child("role").getValue(Boolean.class);
                    if (isAdmin) {
                        GetAllData();
                    } else {
                        GetDataDeliveryList();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", "OnCancelled: " + databaseError.getMessage());
            }
        });
    }

    public void CheckRoleUserForFunction(Oder oder) {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isAdmin = dataSnapshot.child("role").getValue(Boolean.class);
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

    private void GetAllData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myReference = firebaseDatabase.getReference("list_oder");
        myReference.orderByChild("status").equalTo("delivery").addValueEventListener(new ValueEventListener() {
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

    private void GetDataDeliveryList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("list_oder");

        myReference.orderByChild("status").equalTo("delivery").addValueEventListener(new ValueEventListener() {
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
        btnCancel.setText("Xác nhận đã nhận được hàng");
        btnCancel.setOnClickListener(view -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myReference = firebaseDatabase.getReference("list_oder").child(oder.getId());
            oder.setStatus("done");
            myReference.child("status").setValue(oder.getStatus());
            dialog.cancel();
        });
        btnExit.setOnClickListener(view -> {
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
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(view -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myReference = firebaseDatabase.getReference("list_oder").child(oder.getId());
            oder.setStatus("done");
            myReference.child("status").setValue(oder.getStatus());
            Toast.makeText(getContext(), "Hoàn thành đơn hàng " + oder.getId(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
    }

    public void logic(Oder oder) {
        CheckRoleUserForFunction(oder);
    }

}