package com.example.duanthutap.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanthutap.R;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHoder> {
    private List<User> list;
    private Context context;
    private Callback callback;

    public UserAdapter(List<User> list, Context context, Callback callback) {
        this.list = list;
        this.context = context;
        this.callback = callback;
    }

    public interface Callback {
        void itemUserInfo(User user);
    }

    public void setData(List<User> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_user, parent, false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, @SuppressLint("RecyclerView") int position) {
        int index = position;
        User user = list.get(position);
        if (user == null) {
            return;
        }
        if (user.getImg() == null || user.getImg().isEmpty()) {
            holder.img.setImageResource(R.drawable.baseline_perm_identity_24);
        } else {
            Picasso.get().load(user.getImg()).into(holder.img);
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.itemUserInfo(user);
                }
            }
        });
        holder.name.setText("Name :" + user.getName());
        holder.email.setText("Email : " + user.getEmail());
        if (user.getPhoneNumber().isEmpty()||user.getPhoneNumber()==null){
            holder.phonenumber.setText("Phone : Chưa cập nhật số điện thoại" );
        }else {
            holder.phonenumber.setText("Phone : " + user.getPhoneNumber());
        }


        holder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public static class MyViewHoder extends RecyclerView.ViewHolder {
        private ImageView img;
        private ImageButton deleteUser;
        private TextView name, email, phonenumber, id;


        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.user_image);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            phonenumber = itemView.findViewById(R.id.user_phone_number);
            deleteUser = itemView.findViewById(R.id.btn_delete_user);

        }
    }

    public void deleteUser(int position) {
        User user = list.get(position);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {
                                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Xóa người dùng thành công
                                            deleteUserFromRealtimeDatabase(user.getId());
                                            list.remove(position);
                                            notifyDataSetChanged();
                                        } else {

                                            Exception exception = task.getException();
                                            Log.d("TAG", "onComplete: "+exception.getMessage());
                                        }
                                    }
                                });
                            }
                        } else {
                            // Xảy ra lỗi khi đăng nhập
                            Exception exception = task.getException();
                            Log.d("TAG", "onComplete: "+exception.getMessage());
                        }
                    }
                });
    }

    private void deleteUserFromRealtimeDatabase(String userId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("user");

        myRef.child(userId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Xoá người dùng thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Xoá người dùng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
