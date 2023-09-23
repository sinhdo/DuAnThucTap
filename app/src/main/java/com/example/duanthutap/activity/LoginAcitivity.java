package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.duanthutap.MainActivity;
import com.example.duanthutap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAcitivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextInputEditText edEmail;
    private TextInputEditText edPass;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acitivity);

        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(LoginAcitivity.this, MainActivity.class));
//            finish();
//        }
        edEmail = (TextInputEditText) findViewById(R.id.ed_email);
        edPass = (TextInputEditText) findViewById(R.id.ed_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
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
                    .addOnCompleteListener(LoginAcitivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                    Toast.makeText(LoginAcitivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(LoginAcitivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }
}