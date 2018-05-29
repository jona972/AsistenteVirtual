package com.example.jona.asistentevirtual.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.jona.asistentevirtual.R;

import java.util.List;

public class AttractiveAdpater extends PagerAdapter {

    private Context context;
    private List<String> listImagenURL;

    public AttractiveAdpater(Context context, List<String> listImagenURL) {
        this.context = context;
        this.listImagenURL = listImagenURL;
    }

    @Override
    public int getCount() {
        if (listImagenURL != null) {
            return listImagenURL.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_attractive_content, container, false);
        ImageView imageView = view.findViewById(R.id.imagePlacesInformation);

        Glide.with(view.getContext()).load(listImagenURL.get(position)).crossFade().centerCrop().into(imageView);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
