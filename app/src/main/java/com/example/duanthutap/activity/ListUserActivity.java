package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.adapter.UserAdapter;

import com.example.duanthutap.database.FirebaseHelper;

import com.example.duanthutap.model.Product;

import com.example.duanthutap.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
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

public class ListUserActivity extends AppCompatActivity implements UserAdapter.Callback {
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private List<User> list = new ArrayList<>();
    private User user;
    private UserAdapter adapter;

    private ImageButton btnAddUser;
    private Dialog dialogUser;
    private AppCompatButton btnSave, btnCancle, btnDelete;
    private TextInputEditText edName, edEmail, edPhone, edAddress, edPass, edRepass;
    private Spinner spinnerRole;
    private String role;
    private int viTriRole;

    private SearchView searchView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        recyclerView = findViewById(R.id.ryc_list_user);

        btnAddUser = findViewById(R.id.btn_add_user);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        searchView = findViewById(R.id.search_user);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new UserAdapter(list, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
        CallApiUser();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //s = searchView.getQuery().toString();
                performSearch (s);
                return true;
            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialogUser(ListUserActivity.this, 0, user);
            }
        });


    }

    private void CallApiUser() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListUserActivity.this, "Get Fail !!!", Toast.LENGTH_SHORT).show();
            }


        });
    }

    void OpenDialogUser(Context context, int type, User user) {

        dialogUser = new Dialog(context);
        Window window = dialogUser.getWindow();
        if (window == null) {
            return;
        }
        dialogUser.setContentView(R.layout.dialog_user);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        window.setAttributes(windowAttributes);
        //ánh xạ
        edName = dialogUser.findViewById(R.id.edNameUser);
        edEmail = dialogUser.findViewById(R.id.edEmailUser);
        edPhone = dialogUser.findViewById(R.id.edPhoneNumber);
        edAddress = dialogUser.findViewById(R.id.edAddress);
        edPass = dialogUser.findViewById(R.id.edPassword);
        edRepass = dialogUser.findViewById(R.id.edtRePassword);
        btnAddUser = dialogUser.findViewById(R.id.btn_add_user);
        btnCancle = dialogUser.findViewById(R.id.btnCancle);
        btnSave = (AppCompatButton) dialogUser.findViewById(R.id.btnSave);
        btnDelete = dialogUser.findViewById(R.id.btnDelete);
        spinnerRole = dialogUser.findViewById(R.id.spnRole);


        ArrayList<String> listRole = new ArrayList<>();
        listRole.add("ADMIN");
        listRole.add("USER");
        ArrayAdapter arrayAdapterRole = new ArrayAdapter(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listRole);
        spinnerRole.setAdapter(arrayAdapterRole);
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    role =arrayAdapterRole.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUser.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(user.getId());
                dialogUser.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                String phone = edPhone.getText().toString().trim();
                String address = edAddress.getText().toString().trim();
                String password = edPass.getText().toString().trim();
                String repassword = edRepass.getText().toString().trim();


                if (validateRegistration(name, email, phone, password, repassword) || validateRegistration(name, email, phone, user.getPassword(), user.getPassword())) {
                    if (type == 0) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            String id = firebaseUser.getUid();
                                            User user = new User();
                                            user.setId(id);
                                            user.setName(name);
                                            user.setImg("");
                                            user.setEmail(email);
                                            user.setPhoneNumber(phone);
                                            user.setPassword(password);
                                            user.setAddress(address);
                                            if (role=="ADMIN"){
                                                user.setRole(true);
                                            }else {
                                                user.setRole(false);
                                            }
                                            usersRef = firebaseHelper.getUsersRef();
                                            usersRef.child(id).setValue(user);
                                            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                                            Log.d("===TAG", "onComplete: ");
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {

                        user.setName(name);
                        user.setEmail(email);
                        user.setPhoneNumber(phone);
                        user.setAddress(address);
                        UpDateUser(user);
                        dialogUser.dismiss();
                    }
                    dialogUser.dismiss();
                }

            }
        });
        if (type == 1) {
            edName.setText(user.getName());
            edEmail.setText(user.getEmail());
            edPhone.setText(user.getPhoneNumber());
            edAddress.setText(user.getAddress());
            edPass.setVisibility(View.GONE);
            edRepass.setVisibility(View.GONE);
            btnSave.setText("UPDATE");
            btnDelete.setVisibility(View.VISIBLE);
                if (user.getRole()==true){
                    spinnerRole.setSelection(0);
                }else {
                    spinnerRole.setSelection(1);
                }
        }
        if (!isFinishing()) {
            dialogUser.show();
        }
    }

    @Override
    public void itemUserInfo(User user) {
        OpenDialogUser(ListUserActivity.this, 1, user);
    }

    private void UpDateUser(User user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user");

        String id = user.getId();
        if (id != null) {
            myRef.child(id).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error == null) {
                        Toast.makeText(getApplicationContext(), "Update người dùng thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "ID NULL", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser(String id) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user");
        myRef.child(id).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi lưu sản phẩm
                    Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean validateRegistration(String username, String email, String phone, String password, String repass) {
        String nameForm1 = "^[a-zA-Z]+$";
        String nameForm2 = "^[a-zA-Z]+( [a-zA-Z]+)*$";

        if (username.isEmpty() || username == null) {
            edName.setError("Vui lòng nhập tên");
            return false;
        }
        if (!username.matches(nameForm1) || !username.matches(nameForm2)) {
            edName.setError("Tên không đúng định dạng!");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Vui lòng nhập email");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            edEmail.setError("Email không hợp lệ");
            return false;
        }


        if (TextUtils.isEmpty(phone)) {
            edPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        String phoneRegex = "^[0-9]{10}$";
        if (!phone.matches(phoneRegex)) {
            edPhone.setError("Số điện thoại không hợp lệ");
            return false;
        }
        if (password.isEmpty()) {
            edPass.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        if (password.length() < 6) {
            edPass.setError("Mật khẩu phải hơn 6 kí tự");
            return false;
        }
        if (!password.matches(repass)) {
            edRepass.setError("Mật khẩu nhập lại không trùng");
            return false;
        }
        return true;
    }

    private boolean validateRegistration2(String username, String email, String phone) {
        String nameForm1 = "^[a-zA-Z]+$";
        String nameForm2 = "^[a-zA-Z]+( [a-zA-Z]+)*$";

        if (username.isEmpty() || username == null) {
            edName.setError("Vui lòng nhập tên");
            return false;
        }
        if (!username.matches(nameForm1) || !username.matches(nameForm2)) {
            edName.setError("Tên không đúng định dạng!");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Vui lòng nhập email");
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            edEmail.setError("Email không hợp lệ");
            return false;
        }


        if (TextUtils.isEmpty(phone)) {
            edPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        String phoneRegex = "^[0-9]{10}$";
        if (!phone.matches(phoneRegex)) {
            edPhone.setError("Số điện thoại không hợp lệ");
            return false;
        }
        return true;
    }



    private void performSearch(String query) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user");
        Query searchQuery = myRef.orderByChild("email").startAt(query).endAt(query + "\uf8ff");

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        list.add(user); // Thêm kết quả tìm kiếm vào danh sách
                    }
                }
                adapter.setData(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loi", "onCancelled: "+databaseError.getMessage());
            }
        });
    }

}