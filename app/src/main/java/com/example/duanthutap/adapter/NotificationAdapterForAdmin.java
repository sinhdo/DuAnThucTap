package com.example.duanthutap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanthutap.R;
import com.example.duanthutap.database.FirebaseRole;
import com.example.duanthutap.model.Oder;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapterForAdmin extends RecyclerView.Adapter<NotificationAdapterForAdmin.NotifyViewHolder>{
    private Context context;
    private List<Oder> list;
    private FirebaseUser firebaseUser;

    public NotificationAdapterForAdmin(Context context, List<Oder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        Oder oder = list.get(position);
        if (oder == null){
            return;
        }
        if (oder.getStatus().equals("delivery")){
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã "+oder.getId()+" đã được xác nhận. Đang tiến hành giao hàng !!!" );
        } else if (oder.getStatus().equals("done")) {
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã "+oder.getId()+" đã được giao thành công ");
        }else if (oder.getStatus().equals("canceled")){
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã "+oder.getId()+" đã bị huỷ");
        }else if (oder.getStatus().equals("pending")) {
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã " + oder.getId() + " cần được xác nhận !!!");
        }

    }

    @Override
    public int getItemCount() {
        if (list == null){
            return 0;
        }
        return list.size();
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvNotification;
        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
           tvNotification = itemView.findViewById(R.id.tv_notification);
        }
    }
    public void setRoleListUser(){
        String id = firebaseUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isAdmin = FirebaseRole.isUserAdmin(dataSnapshot);
                if (isAdmin) {

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Loi", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

}
