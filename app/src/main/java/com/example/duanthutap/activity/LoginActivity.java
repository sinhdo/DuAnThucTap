package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duanthutap.AdminActivity;
import com.example.duanthutap.MainActivity;
import com.example.duanthutap.R;
import com.example.duanthutap.database.FirebaseRole;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextInputEditText edEmail;
    private TextInputEditText edPass;
    private TextView tvGoToSignUp,tvForgetPass;

    private Button btnLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        tvForgetPass = findViewById(R.id.tv_forgetPass);
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(LoginAcitivity.this, MainActivity.class));
//            finish();
//        }
        edEmail = (TextInputEditText) findViewById(R.id.ed_email);
        edPass = (TextInputEditText) findViewById(R.id.ed_pass);
        btnLogin = findViewById(R.id.btn_login);
        tvGoToSignUp = (TextView) findViewById(R.id.tv_goToSignUp);
        btnLogin.setOnClickListener(this);
        tvGoToSignUp.setOnClickListener(this);
        tvForgetPass.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        mAuth = FirebaseAuth.getInstance();
        if(view.getId()==R.id.btn_login){
            String email = edEmail.getText().toString();
            final String password = edPass.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            //authenticate user
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Đăng nhập thành công
                                FirebaseUser user = mAuth.getCurrentUser();
                                String id = user.getUid();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            boolean isAdmin = FirebaseRole.isUserAdmin(dataSnapshot);
                                            if (isAdmin) {
                                                // Người dùng là Admin
                                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                            } else {
                                                // Người dùng không phải là Admin
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Xử lý lỗi nếu cần
                                        Log.d("Loi login", "Loi casi dit me may "+databaseError.getMessage());
                                    }
                                });
                            } else {
                                // Đăng nhập không thành công
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else if(view.getId()==R.id.tv_goToSignUp){
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            finishAffinity();
        } else if (view.getId()==R.id.tv_forgetPass) {
            startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
        }
    }
}
