package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText edEmail;
    private Button btnSend;
    private ProgressBar progressBar;
    private ImageView imgBack;
    public User user ;
    private static final int PROGRESS_DELAY = 5000; // Thời gian đặt trước 5 giây
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
    }
    public void resetUserPassword(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar khi bắt đầu

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Sử dụng Handler để đặt trễ 5 giây trước khi ẩn ProgressBar
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar khi hoàn thành
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Đã gửi xác nhận đến Email",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Email không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, PROGRESS_DELAY);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE); // Ẩn ProgressBar nếu xảy ra lỗi
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void init(){
        edEmail = (TextInputEditText) findViewById(R.id.ed_email);
        btnSend = (Button) findViewById(R.id.btn_send);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(v->{
            finish();
        });
        btnSend.setOnClickListener(v->{
            String email = edEmail.getText().toString().trim();
            if(email.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            }else{
                resetUserPassword(email);
            }
        });
    }

}