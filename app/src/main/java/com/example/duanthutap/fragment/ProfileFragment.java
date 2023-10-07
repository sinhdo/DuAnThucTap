package com.example.duanthutap.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duanthutap.Order.OrderActivity;
import com.example.duanthutap.R;
import com.example.duanthutap.activity.ChatBoxActivity;
import com.example.duanthutap.activity.InfoUserActivity;
import com.example.duanthutap.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private Button btnLogout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView tvName;
    private TextView tvEmail;
    private LinearLayout lnlOrder;
    private DatabaseReference mReference;
    private ImageView imgAvatarUsers;
    private LinearLayout idInfoUsers;
    private Picasso picasso = Picasso.get();

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
        imgAvatarUsers = (ImageView) view.findViewById(R.id.img_avatarUsers);
        lnlOrder = (LinearLayout) view.findViewById(R.id.lnl_order);
        idInfoUsers = (LinearLayout) view.findViewById(R.id.id_infoUsers);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        mReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","");
        String email = sharedPreferences.getString("email", "");
        String img = sharedPreferences.getString("img", "");
        if (name.isEmpty()){
            setInfoProfile();
        } else {
            tvName.setText(name);
            tvEmail.setText(email);
            if (img.equals("")) {
                imgAvatarUsers.setImageResource(R.drawable.ic_google);
            } else {
                picasso.load(img).into(imgAvatarUsers);
            }
        }

        btnLogout.setOnClickListener(this);
        lnlOrder.setOnClickListener(this);
        idInfoUsers.setOnClickListener(this);
    }

    private void setInfoProfile() {
        String id = firebaseUser.getUid();
        DatabaseReference userRef = mReference.child("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String img = snapshot.child("img").getValue(String.class);
                tvName.setText(name);
                tvEmail.setText(email);
                if (img.equals("")) {
                    imgAvatarUsers.setImageResource(R.drawable.ic_google);
                } else {
                    picasso.load(img).into(imgAvatarUsers);
                }
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("img", img);
                editor.apply();
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
        } else if (view.getId() == R.id.lnl_order) {
            startActivity(new Intent(getActivity(), OrderActivity.class));
        } else if (view.getId() == R.id.id_infoUsers) {
            startActivity(new Intent(getActivity(), InfoUserActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setInfoProfile();
    }
}