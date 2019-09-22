package com.itzb.anim.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ParallaxFragment extends Fragment {

    private List<View> views = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getArguments().getInt("layoutId");
        ParallaxLayoutInflater layoutInflater = new ParallaxLayoutInflater(inflater, getActivity(), this);
        return layoutInflater.inflate(layoutId, null);
    }

    public List<View> getViews() {
        return views;
    }
}
