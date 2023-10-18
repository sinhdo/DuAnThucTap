package com.example.duanthutap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanthutap.R;
import com.example.duanthutap.model.Oder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OderAdapter extends RecyclerView.Adapter<OderAdapter.BillViewHolder>{
    private Context context;
    private List<Oder> oderList;
    private Callback callback;

    public OderAdapter(Context context, List<Oder> oderList, Callback callback) {
        this.context = context;
        this.oderList = oderList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oder,parent,false);
        return new BillViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Oder oder = oderList.get(position);
        if (oder == null){
            return;
        }
        holder.tvNameProduct.setText(oder.getName());
        holder.tvTotal.setText(String.valueOf(oder.getTotal()));
        holder.tvPhone.setText(oder.getPhone_number());
        holder.tvDate.setText(oder.getDate());
        holder.tvStatus.setText(oder.getStatus());
        Picasso.get().load(oder.getImage()).into(holder.imgProduct);
        holder.imgMenu.setOnClickListener(view -> {
            callback.logic(oder);
        });
    }

    @Override
    public int getItemCount() {
        if (oderList == null){
            return 0;
        }
        return oderList.size();
    }

    public class BillViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvTotal;
        private TextView tvPhone;
        private TextView tvDate;
        private TextView tvStatus;
        private ImageView imgMenu;
        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tv_nameProduct);
            tvTotal = (TextView) itemView.findViewById(R.id.tv_total);
            tvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            imgMenu = (ImageView) itemView.findViewById(R.id.img_menu);
        }
    }
    public interface Callback{
        void logic(Oder oder);
    }
}
