package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.presentation.activity.MyAdapter;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;

public class MessagesFragment extends BaseFragment<MessagesFragment> {

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //set adapter
        //listItems.clear();
        adapter = new MyAdapter(listItems, view.getContext(), R.menu.inbox_menu, new MenuItemSelector() {
            @Override
            public void onItemSelect(MenuItem item, int position) {
                switch (item.getItemId()) {
                    case R.id.mnu_item_mark_as_important:
                        Toast.makeText(adapter.getContext(), "this call has been marked as important", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.mnu_item_delete:
                        adapter.getListItems().remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });

        addItem("Inbox", "inbox items", 1l);
        addItem("Sim inbox", "sim card messages", 2l);
        addItem("Sent", "sent items", 3l);
    }

}
