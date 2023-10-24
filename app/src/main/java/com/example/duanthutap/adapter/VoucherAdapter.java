package com.example.duanthutap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanthutap.databinding.ItemMagiamBinding;
import com.example.duanthutap.model.Voucher;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {


    private List<Voucher> magiam;
    private onItemClick onClick;

    public VoucherAdapter(List<Voucher> magiam, onItemClick onClick) {
        this.magiam = magiam;
        this.onClick = onClick;
    }

    public interface onItemClick{
        void onItemSelected(Voucher voucher);

    }
    public void setData(List<Voucher> magiam){
        this.magiam = magiam;
        notifyDataSetChanged();
    }

    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMagiamBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voucher object = magiam.get(position);
        holder.binding.tvVoucher.setText(object.getName());
        Picasso.get().load(object.getImg()).into(holder.binding.imgVoucher);
        if(object.isSelected()){
            holder.binding.btnCheckbox.setVisibility(View.VISIBLE);
        }else{
            holder.binding.btnCheckbox.setVisibility(View.GONE);
        }
        holder.binding.getRoot().setOnClickListener(view -> {
            onClick.onItemSelected(object);
            object.setSelected(true);
            for (int i = 0; i < magiam.size(); i++) {
                if (magiam.get(i).getId() != object.getId()) {
                    magiam.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();

        });

    }

    @Override
    public int getItemCount() {
        return magiam.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMagiamBinding binding;

        public ViewHolder(ItemMagiamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
