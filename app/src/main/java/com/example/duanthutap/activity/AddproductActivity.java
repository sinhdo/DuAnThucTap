package com.example.duanthutap.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duanthutap.R;
import com.example.duanthutap.model.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddproductActivity extends AppCompatActivity {
    private TextInputEditText edName;
    private TextInputEditText edCategory;
    private ImageView imgProduct;
    private TextInputEditText edDes;
    private TextInputEditText edPrice;
    private TextInputEditText edQuantity;
    private Button btnAdd;
    private Button btnCanel;
    private String selectedImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        init();
    }
    private void onClickAdd(Product product){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_product");

        String id = product.getId();
        myRef.child(id).setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Thành công, hiển thị ảnh trong ImageView
                    imgProduct.setImageURI(Uri.parse(selectedImageUrl));
                    Toast.makeText(AddproductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi lưu sản phẩm
                    Toast.makeText(AddproductActivity.this, "Lỗi khi lưu sản phẩm vào Realtime Database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void init(){
        edName = (TextInputEditText) findViewById(R.id.ed_name);
        edCategory = (TextInputEditText) findViewById(R.id.ed_category);
        imgProduct = (ImageView) findViewById(R.id.img_product);
        edDes = (TextInputEditText) findViewById(R.id.ed_des);
        edPrice = (TextInputEditText) findViewById(R.id.ed_price);
        edQuantity = (TextInputEditText) findViewById(R.id.ed_quantity);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnCanel = (Button) findViewById(R.id.btn_canel);

        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        btnAdd.setOnClickListener(v1->{
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = firebaseDatabase.getReference("list_product");
            String newKey = myRef.push().getKey();
            String name = edName.getText().toString().trim();
            String category = edCategory.getText().toString().trim();
            String price = edPrice.getText().toString().trim();
            String  des= edDes.getText().toString().trim();
            String quantity = edQuantity.getText().toString().trim();
            if(name.isEmpty()||category.isEmpty()||price.isEmpty()||des.isEmpty()||quantity.isEmpty()){
                Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
            } else if (selectedImageUrl == null || selectedImageUrl.isEmpty()) {
                Toast.makeText(AddproductActivity.this, "Vui lòng chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
            } else{
                Product product = new Product(newKey,name,category,des, selectedImageUrl,"",Double.parseDouble(price),2,Integer.parseInt(quantity));
                onClickAdd(product);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Đường dẫn của ảnh được chọn
            Uri selectedImageUri = data.getData();
            imgProduct.setImageURI(selectedImageUri);
            selectedImageUrl = selectedImageUri.toString();
        }
    }

}