package com.itzb.anim.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class ParallaxLayoutInflater extends LayoutInflater {

    private static final String TAG = "ParallaxLayoutInflater";
    private ParallaxFragment fragment;

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext, ParallaxFragment fragment) {
        super(original, newContext);
        this.fragment = fragment;

        //监听view被填充
        setFactory2(new ParallaxFactory(this));
    }

    //重写LayoutInflater必须重写此方法
    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this, newContext, fragment);
    }


    /**
     * ParallaxFactory监听View从xml加载到内存
     */
    private class ParallaxFactory implements Factory2 {

        private LayoutInflater layoutInflater;

        //加载的系统View都在这两个包下
        private final String[] sClassPrefix = {
                "android.widget.",
                "android.view."
        };

        //自定义的属性
        int[] attrIds = {
                R.attr.a_in,
                R.attr.a_out,
                R.attr.x_in,
                R.attr.x_out,
                R.attr.y_in,
                R.attr.y_out
        };


        public ParallaxFactory(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        @SuppressLint("ResourceType")
        @Override
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
            View view = createMyView(name, context, attrs);
            if (view != null) {
                TypedArray a = context.obtainStyledAttributes(attrs, attrIds);
                if (a != null && a.length() > 0) {
                    //获取自定义属性的值
                    ParallaxViewTag tag = new ParallaxViewTag();
                    tag.alphaIn = a.getFloat(0, 0f);
                    tag.alphaOut = a.getFloat(1, 0f);
                    tag.xIn = a.getFloat(2, 0f);
                    tag.xOut = a.getFloat(3, 0f);
                    tag.yIn = a.getFloat(4, 0f);
                    tag.yOut = a.getFloat(5, 0f);
                    view.setTag(R.id.parallax_view_tag, tag);
                }
                fragment.getViews().add(view);
                a.recycle();
            }
            Log.d(TAG, "onCreateView: ");
            return view;
        }

        private View createMyView(String name, Context context, AttributeSet attrs) {
            if (name.contains(".")) {//自定义的空间
                return reflectView(name, null, context, attrs);
            } else {//系统空间
                for (String classPrefix : sClassPrefix) {
                    View view = reflectView(name, classPrefix, context, attrs);
                    if (view != null) {
                        return view;
                    }
                }
            }
            return null;
        }

        private View reflectView(String name, String prefix, Context context, AttributeSet attrs) {
            try {
                //通过系统的inflater创建视图，读取系统的属性
                return layoutInflater.createView(name, prefix, attrs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return null;
        }
    }
}
