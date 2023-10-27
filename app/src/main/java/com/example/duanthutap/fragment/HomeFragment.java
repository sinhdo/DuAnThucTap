package com.example.duanthutap.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.duanthutap.R;
import com.example.duanthutap.activity.ChatBoxActivity;
import com.example.duanthutap.adapter.SliderAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private Timer timer;
    private ViewPager vpgSlideImage;
    private CircleIndicator circleIndicator;
    private RecyclerView rcvListitem;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vpgSlideImage = (ViewPager) view.findViewById(R.id.vpg_slide_image);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        rcvListitem = (RecyclerView) view.findViewById(R.id.rcv_listitem);

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.banner1);
        imageList.add(R.drawable.banner2);
        imageList.add(R.drawable.banner3);
        imageList.add(R.drawable.banner4);


        SliderAdapter sliderAdapter = new SliderAdapter(getActivity(), imageList);
        vpgSlideImage.setAdapter(sliderAdapter);

        circleIndicator.setViewPager(vpgSlideImage);
        sliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int currentItem = vpgSlideImage.getCurrentItem();
                            int totalItems = vpgSlideImage.getAdapter().getCount();
                            int nextItem = (currentItem + 1) % totalItems;
                            vpgSlideImage.setCurrentItem(nextItem);
                        }
                    });
                }
            }
        }, 2000, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }
}