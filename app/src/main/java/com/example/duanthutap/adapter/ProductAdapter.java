package com.example.duanthutap.adapter;


import static java.lang.Double.parseDouble;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duanthutap.R;
import com.example.duanthutap.model.Product;
import com.example.duanthutap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Context;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> mList;
    private Context context;
    int num;

    public ProductAdapter(Context context,List<Product> mList) {
        this.context=context;
        this.mList = mList;
        notifyDataSetChanged();
    }
    public void setProductList(List<Product> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = mList.get(position);
        if(product==null){
            return;
        }
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice()+"");
        if(product.getQuantity()>0){
            holder.tvQuantity.setText("Còn hàng");
        }else{
            holder.tvQuantity.setText("hết hàng");
        }
        Picasso.get().load(product.getImg()).into(holder.imgProduct);
        holder.imgProduct.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.FullScreenDialogTheme);
            LayoutInflater inflater = LayoutInflater.from(v.getContext());
            View dialogView = inflater.inflate(R.layout.dialog_product, null);
            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();
            dialog.show();
            final ImageView[] imgFood = {(ImageView) dialog.findViewById(R.id.imgFood)};
            TextView tvName = (TextView) dialog.findViewById(R.id.tv_name);
            TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_price);
            ImageView  imgMinus = (ImageView) dialog.findViewById(R.id.img_minus);
            ImageView  imgPlus = (ImageView) dialog.findViewById(R.id.img_plus);
            TextView  tvDes = (TextView) dialog.findViewById(R.id.tv_des);
            TextView tvNum = (TextView) dialog.findViewById(R.id.tv_num);
            TextView  tvQuantity = (TextView) dialog.findViewById(R.id.tv_quantity);
            TextView tvTotalPrice = (TextView) dialog.findViewById(R.id.tvTotalPrice);
            TextView tvAddToCart = (TextView) dialog.findViewById(R.id.tvAddToCart);

            num =1;

            // Ban đầu, tính và hiển thị tổng giá tiền
            String imgUrl = getItem(holder.getAdapterPosition()).getImg();
            Picasso.get().load(imgUrl).into(imgFood[0]);
            tvName.setText(product.getName());
            tvPrice.setText(product.getPrice()+"VNĐ");
            tvDes.setText(product.getDescription());
            tvQuantity.setText("Kho: "+ product.getQuantity());
            tvNum.setText(num+"");
            tvTotalPrice.setText(num*product.getPrice()+" VND");

            imgMinus.setOnClickListener(view -> {
                if (num > 1){
                    num--;
                    tvNum.setText(num+"");
                    tvTotalPrice.setText(num*product.getPrice()+" VND");
                }
            });
            imgPlus.setOnClickListener(view -> {
                if (num < product.getQuantity()){
                    num++;
                    tvNum.setText(num+"");
                    tvTotalPrice.setText(num*product.getPrice()+" VND");
                }
            });
            tvAddToCart.setOnClickListener(v1->{
                dialog.dismiss();
            });
        });
    }


    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    public Product getItem(int position) {
        return mList.get(position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvQuantity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        }
    }
}
