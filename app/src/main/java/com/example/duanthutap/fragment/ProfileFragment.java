package com.example.duanthutap.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.ListUserActivity;
import com.example.duanthutap.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private Button btnLogout,btnListUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvName;
    private TextView tvEmail;
    private DatabaseReference mReference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = (Button) view.findViewById(R.id.btn_log_out);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvEmail = (TextView) view.findViewById(R.id.tv_email);
        btnListUser = (Button) view.findViewById(R.id.btn_list_user);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mReference = FirebaseDatabase.getInstance().getReference();

        setInfoProfile();

        btnLogout.setOnClickListener(this);
        btnListUser.setOnClickListener(this);
    }

    private void setInfoProfile() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = mReference.child("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                tvName.setText(name);
                tvEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi", "onCancelled: " + error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_log_out) {
            firebaseAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else if (view.getId()==R.id.btn_list_user) {
            startActivity(new Intent(getActivity(), ListUserActivity.class));
        }
    }
}