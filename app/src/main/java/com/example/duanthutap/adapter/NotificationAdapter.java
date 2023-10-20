package com.example.duanthutap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanthutap.R;
import com.example.duanthutap.model.Oder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotifyViewHolder>{
    private Context context;
    private List<Oder> list;

    public NotificationAdapter(Context context, List<Oder> list) {
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
            holder.tvNotification.setText("Đơn hàng mã "+oder.getId()+" của bạn đã được xác nhận. Vui lòng nhấn xác nhận đã nhận hàng khi nhận được hàng \n" +"Cảm ơn quý khách !!!");
        } else if (oder.getStatus().equals("done")) {
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã "+oder.getId()+" đã được giao thành công \n" +"Cảm ơn quý khách đã mua hàng tại EasyShop!!!");
        }else if (oder.getStatus().equals("canceled")){
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã "+oder.getId()+" đã được huỷ thành công !!!");
        }else if (oder.getStatus().equals("pending")) {
            Picasso.get().load(oder.getImage()).into(holder.imgProduct);
            holder.tvNotification.setText("Đơn hàng mã " + oder.getId() + " đã được được đặt. Vui lòng đợi admin xác nhận !!!");
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
    private void deleteOrderCancled(String orderID) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("list_oder");

        myRef.child(orderID).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
