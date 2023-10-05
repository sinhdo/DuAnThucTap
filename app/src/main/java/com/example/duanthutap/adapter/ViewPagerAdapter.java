package com.example.duanthutap.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.duanthutap.Order.DeliveryFragment;
import com.example.duanthutap.Order.PaymentFragment;
import com.example.duanthutap.Order.WaitConfirmFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = WaitConfirmFragment.newInstance();
                break;
            case 1:
                fragment = DeliveryFragment.newInstance();
                break;
            case 2:
                fragment = PaymentFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
