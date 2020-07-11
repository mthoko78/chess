package com.mthoko.mobile.presentation.listener;

import android.content.Intent;
import android.view.View;

import com.mthoko.mobile.R;
import com.mthoko.mobile.presentation.activity.InboxActivity;
import com.mthoko.mobile.presentation.activity.MyAdapter;

import static com.mthoko.mobile.resource.internal.SmsResource.INBOX;
import static com.mthoko.mobile.resource.internal.SmsResource.SENT;
import static com.mthoko.mobile.resource.internal.SmsResource.SIM;

public class ListenerFactory {

    public static final String TITLE_SIM_INBOX = "Sim inbox";
    public static final String TITLE_SENT = "Sent";
    public static final String TITLE_INBOX = "Inbox";

    public static View.OnClickListener getOnclickListener(int menuId, final MyAdapter.ViewHolder viewHolder) {
        switch (menuId) {
            case R.menu.inbox_menu :{
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), InboxActivity.class);
                        String key = "messageType";
                        if (viewHolder.txtTitle.getText().equals(TITLE_INBOX)) {
                            intent.putExtra(key, INBOX);
                        }
                        if (viewHolder.txtTitle.getText().equals(TITLE_SIM_INBOX)) {
                            intent.putExtra(key, SIM);
                        }
                        if (viewHolder.txtTitle.getText().equals(TITLE_SENT)) {
                            intent.putExtra(key, SENT);
                        }
                        if (intent.hasExtra(key)) {
                            view.getContext().startActivity(intent);
                        }
                    }
                };
            }
        }
        return null;
    }

}
