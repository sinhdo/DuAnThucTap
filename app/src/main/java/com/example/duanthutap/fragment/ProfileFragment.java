package com.example.duanthutap.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.ChatBoxActivity;
import com.example.duanthutap.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;



    

import com.example.duanthutap.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

    public class ProfileFragment extends Fragment implements View.OnClickListener {
        private Button btnLogout,chatboxbtn;
        private FirebaseAuth firebaseAuth;

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

        chatboxbtn = view.findViewById(R.id.chatboxbtn);
            btnLogout = (Button) view.findViewById(R.id.btn_logout);
            firebaseAuth = FirebaseAuth.getInstance();
            btnLogout.setOnClickListener(this);
            chatboxbtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_logout) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }else if (view.getId()==R.id.chatboxbtn){
                startActivity(new Intent(getActivity(), ChatBoxActivity.class));
            }
        }
    }