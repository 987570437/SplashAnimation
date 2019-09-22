package com.itzb.anim.splash;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;

public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    private List<ParallaxFragment> fragments = new ArrayList<>();
    private ImageView ivMain;
    private ParallaxPagerAdapter adapter;

    public ParallaxContainer(@NonNull Context context) {
        this(context, null);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIvMain(ImageView ivMain) {
        this.ivMain = ivMain;
    }

    public void setUp(int... childIds) {

        for (int i = 0; i < childIds.length; i++) {
            ParallaxFragment parallaxFragment = new ParallaxFragment();
            Bundle args = new Bundle();
            //Fragment中需要加载的布局文件id
            args.putInt("layoutId", childIds[i]);
            parallaxFragment.setArguments(args);
            fragments.add(parallaxFragment);
        }
        //初始化ViewPager
        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setId(R.id.parallax_pager);
        SplashActivity activity = (SplashActivity) getContext();
        viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        adapter = new ParallaxPagerAdapter(activity.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        addView(viewPager, 0);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        ParallaxFragment outFragment = null;
        ParallaxFragment inFragment = null;
        try {
            outFragment = fragments.get(position - 1);
            inFragment = fragments.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (outFragment != null) {
            List<View> views = outFragment.getViews();

            if (views != null && views.size() > 0) {
                for (View view : views) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    ViewHelper.setTranslationX(view, (getWidth() - positionOffsetPixels) * tag.xOut);
                    ViewHelper.setTranslationY(view, (getWidth() - positionOffsetPixels) * tag.yOut);
                }
            }
        }

        if (inFragment != null) {
            List<View> views = inFragment.getViews();

            if (views != null && views.size() > 0) {
                for (View view : views) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }

                    //退出的fragment中view从原始位置开始向上移动，translationY应为负数
                    ViewHelper.setTranslationX(view, 0 - positionOffsetPixels * tag.xIn);
                    ViewHelper.setTranslationY(view, 0 - positionOffsetPixels * tag.yIn);
                }
            }
        }

    }

    @Override
    public void onPageSelected(int position) {
        ivMain.setVisibility(position == adapter.getCount() - 1 ? GONE : VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable animationDrawable = (AnimationDrawable) ivMain.getBackground();
        if (state == SCROLL_STATE_IDLE) {
            animationDrawable.stop();
        } else if (state == SCROLL_STATE_DRAGGING) {
            animationDrawable.start();
        }
    }
}
