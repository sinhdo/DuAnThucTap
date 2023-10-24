package com.example.duanthutap.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duanthutap.R;
import com.example.duanthutap.model.Product;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import android.content.Context;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> mList;
    private Context context;
    private Callback callback;
    public ProductAdapter(Context context,List<Product> mList, Callback callback) {
        this.context=context;
        this.mList = mList;
        this.callback = callback;
        notifyDataSetChanged();
    }
    public void setProductList(List<Product> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
    public void setCallback(Callback callback) {
        this.callback = callback;
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
                callback.itemProductInfo(product);
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

    public interface Callback{
        void itemProductInfo(Product product);
    }
}
