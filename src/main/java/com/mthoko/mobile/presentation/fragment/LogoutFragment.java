package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mthoko.mobile.R;
import com.mthoko.mobile.presentation.activity.DrawerActivity;

public class LogoutFragment extends BaseFragment<LogoutFragment> {

    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (DrawerActivity.INSTANCE != null) {
            DrawerActivity.INSTANCE.finish();
        }
        return inflater.inflate(R.layout.fragment_logout, container, false);
    }

}
