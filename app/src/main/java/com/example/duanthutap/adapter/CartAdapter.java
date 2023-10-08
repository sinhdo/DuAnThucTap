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
    private Callback callback;
    TextView tv_totalbill;
    public CartAdapter(Context context,List<ProductsAddCart> productsList, TextView tv_totalbill, Callback callback) {
        this.context = context;
        this.productsList = productsList;
        this.tv_totalbill = tv_totalbill;
        this.callback = callback;
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

        holder.tvNameProductCart.setText(products.getName_product());
        holder.tvNumProductCart.setText(products.getNum_product()+"");
        holder.tvPriceProductCart.setText(products.getPricetotal_product()+"");
        Picasso.get().load(products.getImage_product()).into(holder.imgProductCart);
        holder.imgDeleteProductCart.setOnClickListener(view -> {
            callback.deleteItemCart(products);
        });
    }

    @Override
    public int getItemCount() {
        return productsList == null ? 0 : productsList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProductCart;
        private TextView tvNameProductCart;
        private TextView tvPriceProductCart;
        private TextView tvNumProductCart;
        private ImageView imgDeleteProductCart;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductCart = (ImageView) itemView.findViewById(R.id.img_productCart);
            tvNameProductCart = (TextView) itemView.findViewById(R.id.tv_nameProductCart);
            tvPriceProductCart = (TextView) itemView.findViewById(R.id.tv_priceProductCart);
            tvNumProductCart = (TextView) itemView.findViewById(R.id.tv_numProductCart);
            imgDeleteProductCart = (ImageView) itemView.findViewById(R.id.img_deleteProductCart);
        }
    }
    public interface Callback{
        void deleteItemCart(ProductsAddCart products);
    }
}
