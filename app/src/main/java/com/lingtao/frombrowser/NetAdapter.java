package com.lingtao.frombrowser;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NetAdapter extends PagerAdapter {
    private List<HomeBannerEntry> list;
    private Context context;
    private LayoutInflater inflater;

    public NetAdapter(Context context, List<HomeBannerEntry> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.view_pager_home_banner, null);
        ImageView iv = (ImageView) view.findViewById(R.id.viewPager_item_image);
        Glide.with(context).load(list.get(position % list.size())).into(iv);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
