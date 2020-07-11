package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mthoko.mobile.R;
import com.mthoko.mobile.entity.SimContact;
import com.mthoko.mobile.presentation.activity.MyAdapter;
import com.mthoko.mobile.presentation.controller.SimContactsController;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;

import java.util.List;

public class SimContactsFragment extends BaseFragment<SimContactsFragment> {

    private SimContactsController simContactsController;

    public SimContactsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sim_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        String currentSimNo = simContactsController.getCurrentSimNo();
        List<SimContact> simContacts = simContactsController.getSimContactsBySimNo(currentSimNo);
        for (SimContact simContact : simContacts) {
            addItem(simContact.getName(), simContact.getPhone(), simContact.getId());
        }
        refresh();
    }

    private void initComponents(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listItems.clear();
        simContactsController = new SimContactsController(view.getContext());
        adapter = new MyAdapter(listItems, view.getContext(), R.menu.contact_menu, new MenuItemSelector() {
            @Override
            public void onItemSelect(MenuItem item, int position) {

            }
        });
    }

}
