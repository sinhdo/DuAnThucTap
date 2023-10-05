package com.example.duanthutap.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duanthutap.R;
import com.example.duanthutap.fragment.CartFragment;
import com.example.duanthutap.model.ProductsAddCart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<ProductsAddCart> productsList;
    TextView tv_totalbill;
    public CartAdapter(Context context,TextView tv_totalbill) {
        this.context = context;
        this.tv_totalbill = tv_totalbill;
    }

    public void setDataProductsCart(List<ProductsAddCart> productsList) {
        this.productsList = productsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addcart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductsAddCart products = productsList.get(position);
        if (products == null) {
            return;
        }
//        Picasso.get().load(products.getImage_product()).placeholder(R.drawable.shoppingbag).error(R.drawable.shoppingbag).into(holder.img_pro);
        Picasso.get().load(products.getImage_product()).into(holder.img_pro);
        holder.name_pro.setText(products.getName_product());
        holder.num_pro.setText("Số lượng: " + products.getNum_product());
    }

    @Override
    public int getItemCount() {
        return productsList == null ? 0 : productsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView img_pro;
        TextView name_pro, num_pro;
        ImageView img_delete;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            img_pro = itemView.findViewById(R.id.img_pro);
            name_pro = itemView.findViewById(R.id.name_pro);
            num_pro = itemView.findViewById(R.id.num_pro);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
