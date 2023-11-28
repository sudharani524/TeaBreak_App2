package com.example.teabreak_app.Ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.teabreak_app.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;

    private List<Integer> urlList;

    public SliderAdapter(Context context, List<Integer> url) {
        this.context = context;

        this.urlList = url;
    }

    @Override
    public int getCount() {

        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.image_slider, null);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView=(ImageView)view.findViewById(R.id.iv_auto_image_slider);
        Glide.with(imageView)
                .load(urlList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imageView);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}

