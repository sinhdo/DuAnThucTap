package com.example.duanthutap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duanthutap.R;
import com.example.duanthutap.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter   extends RecyclerView.Adapter<UserAdapter.MyViewHoder>{
    private List<User> list;
    private Context context;

    public UserAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_user,parent,false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        int index = position;
        User user = list.get(position);
        if (user==null){
            return;
        }
        if (user.getImg() == null || user.getImg().isEmpty()) {
            holder.img.setImageResource(R.drawable.baseline_perm_identity_24);
        } else {
            Picasso.get().load(user.getImg()).into(holder.img);
        }

//        holder.id.setText("ID : "+user.getId());
        holder.name.setText("Name :"+user.getName());
        holder.email.setText("Email : "+user.getEmail());
        holder.phonenumber.setText("Phone : "+user.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }


    public static class MyViewHoder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView name,email,phonenumber,id;


        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.user_image);
            name=itemView.findViewById(R.id.user_name);
            id=itemView.findViewById(R.id.user_id);
            email=itemView.findViewById(R.id.user_email);
            phonenumber=itemView.findViewById(R.id.user_phone_number);

        }
    }

}
