package com.myapplication.utils;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.myapplication.MidSectionActivity;
import com.myapplication.TetradActivity;

public class TetradActivityPreHandler implements PreHandle{

    private Fragment fragment;
    private final Class<TetradActivity> targetActivityClazz = TetradActivity.class;
    private final String targetActivityName = TetradActivity.class.getSimpleName();

    @Override
    public void handle() {
        Intent intent = new Intent(fragment.getContext(),targetActivityClazz);
        fragment.startActivity(intent);

    }

    @Override
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
