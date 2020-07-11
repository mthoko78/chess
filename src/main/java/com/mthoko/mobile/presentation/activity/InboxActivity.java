package com.mthoko.mobile.presentation.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.model.RecyclerItem;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.service.SmsService;

import java.util.ArrayList;
import java.util.List;

import static com.mthoko.mobile.resource.internal.MyResources.showMessage;
import static com.mthoko.mobile.resource.internal.SmsResource.INBOX;
import static com.mthoko.mobile.resource.internal.SmsResource.SENT;
import static com.mthoko.mobile.resource.internal.SmsResource.SIM;

public class InboxActivity extends AppCompatActivity {

    public static InboxActivity INSTANCE;
    RecyclerView recyclerView;
    MyAdapter adapter;
    final List<RecyclerItem> listItems = new ArrayList<>();
    private SmsService smsService;
    private String messageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        setContentView(R.layout.activity_inbox);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        messageType = getIntent().getStringExtra("messageType");
        smsService = ServiceFactory.getSmsService(this.getApplicationContext());
        showMessage(this, "Calling activity", "" + getParent());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //set adapter
        //listItems.clear();
        adapter = new MyAdapter(listItems, this, R.menu.sms_menu, new MenuItemSelector() {
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

        List<Sms> msgs = null;
        if (INBOX.equals(messageType)) {
            msgs = smsService.getActualInbox();
        } else if (SIM.equals(messageType)) {
            msgs = smsService.getActualSimInbox();
        } else if (SENT.equals(messageType)) {
            msgs = smsService.getActualSent();
        }

        for (Sms sms : msgs) {
            addItem(sms.getSender() + (sms.isRead() ? "" : " **new"), sms.getFormattedString(), sms.getId(), false);
        }
        refresh();
    }

    @Override
    public boolean onNavigateUp() {
        showMessage(this, "gg", "ggg");
        return false;
    }

    public void addItem(String title, String body, Long id) {
        addItem(title, body, id, true);
    }

    public void addItem(String title, String body, Long id, boolean refresh) {
        listItems.add(new RecyclerItem(title, body, id));
        if (refresh)
            refresh();
    }

    public void refresh() {
        recyclerView.setAdapter(adapter);
    }
}
