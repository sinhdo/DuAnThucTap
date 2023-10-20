package com.example.duanthutap.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.NotificationAdapter;
import com.example.duanthutap.adapter.OderAdapter;
import com.example.duanthutap.model.Oder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class NotificationFragment extends Fragment {
    RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private ArrayList<Oder> list = new ArrayList<>();
    private FirebaseUser firebaseUser;


    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
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
        return inflater.inflate(R.layout.fragment_notification, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcy_notificaton);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getNotification();
        adapter = new NotificationAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);
    }
    private void getNotification(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String id_user = firebaseUser.getUid();
        DatabaseReference myReference = firebaseDatabase.getReference("list_oder");

        myReference.orderByChild("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Oder oder = dataSnapshot.getValue(Oder.class);
                    if (oder.getId_user().equals(id_user)){
                        list.add(0,oder);
                    }
                }
//                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Get list users failed", Toast.LENGTH_SHORT).show();
                Log.d("LIST-CART", "onCancelled: " + error.getMessage());
            }

        });
        myReference.orderByChild("status").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Oder updatedItem = snapshot.getValue(Oder.class);
                int index = -1;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(updatedItem.getId())) {
                        if (updatedItem.getId_user().equals(id_user)){
                            index = i;
                            break;
                        }

                    }
                }
                if (index != -1) {
                    list.set(index, updatedItem);
                    adapter.notifyItemChanged(index);
                }
                Log.d("===========", "onChildChanged: "+index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}