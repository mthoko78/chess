package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mthoko.mobile.R;
import com.mthoko.mobile.presentation.activity.MyAdapter;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;

public class DashboardFragment extends BaseFragment<SettingsFragment> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //set adapter
        adapter = new MyAdapter(listItems, view.getContext(), R.menu.recorded_call_menu, new MenuItemSelector() {
            @Override
            public void onItemSelect(MenuItem item, int position) {
            }
        });
    }
}
