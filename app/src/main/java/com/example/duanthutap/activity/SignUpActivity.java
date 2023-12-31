package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.duanthutap.MainActivity;
import com.example.duanthutap.R;
import com.example.duanthutap.database.FirebaseHelper;
import com.example.duanthutap.databinding.ActivityMainBinding;
import com.example.duanthutap.databinding.ActivitySignUpBinding;
import com.example.duanthutap.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        binding.btnSignUp.setOnClickListener(this);
        binding.tvGoSignIn.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signUp){
            String name = binding.edName.getText().toString().trim();
            String email = binding.edEmail.getText().toString().trim();
            String pass = binding.edPass.getText().toString().trim();
            String rePass = binding.edRePass.getText().toString().trim();

            if (checkValidate(name,email,pass,rePass)){
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()){
                                    //tao user vao realtime
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    String id = firebaseUser.getUid();
                                    User user = new User();
                                    user.setName(name);
                                    user.setEmail(email);
                                    user.setPassword(pass);
                                    user.setId(id);
                                    user.setPhoneNumber("");
                                    user.setImg("");
                                    user.setAddress("");
                                    user.setRole(false);
                                    usersRef = firebaseHelper.getUsersRef();
                                    usersRef.child(id).setValue(user);

                                    Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    finishAffinity();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else if (view.getId() == R.id.tv_goSignIn) {
             startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
             finishAffinity();
        }
    }

    private boolean checkValidate(String name, String email, String pass, String rePass){
        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Email, Password, RePassword không được để trống!", Toast.LENGTH_SHORT).show();
            return false;
        }

        String emailForm = "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$";
        String nameForm1 = "^[a-zA-Z]+$";
        String nameForm2 = "^[a-zA-Z]+( [a-zA-Z]+)*$";
        
        if (!name.matches(nameForm1) || !name.matches(nameForm2)){
            Toast.makeText(this, "Tên không đúng định dạng!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.matches(emailForm)){
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() < 6 || pass.length() > 12){
            Toast.makeText(this, "Mật khẩu phải có 6 đến 12 ký tự!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!pass.equals(rePass)){
            Toast.makeText(this, "Xác nhận mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}