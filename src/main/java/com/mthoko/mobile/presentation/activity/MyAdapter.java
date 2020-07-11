package com.mthoko.mobile.presentation.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mthoko.mobile.R;
import com.mthoko.mobile.model.RecyclerItem;
import com.mthoko.mobile.presentation.listener.ListenerFactory;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;

import java.util.List;

/**
 * Created by Mthoko on 03 May 2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final MenuItemSelector menuItemSelector;

    public List<RecyclerItem> getListItems() {
        return listItems;
    }

    private List<RecyclerItem> listItems;
    private Context mContext;
    private int menuId;

    public MyAdapter(List<RecyclerItem> listItems, Context mContext, int menuId, MenuItemSelector menuItemSelector) {
        this.menuItemSelector = menuItemSelector;
        this.listItems = listItems;
        this.mContext = mContext;
        this.menuId = menuId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(ListenerFactory.getOnclickListener(menuId, viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RecyclerItem recyclerItem = listItems.get(position);
        holder.txtTitle.setText(recyclerItem.getTitle());
        holder.txtDescription.setText(recyclerItem.getDescription());
        holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), holder.txtOptionDigit);
                popupMenu.inflate(getMenuId());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        menuItemSelector.onItemSelect(item, position);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public Context getContext() {
        return mContext;
    }

    public int getMenuId() {
        return menuId;
    }

    public void deleteItemAt(int position) {
        getListItems().remove(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public TextView txtOptionDigit;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtOptionDigit = itemView.findViewById(R.id.txtOptionDigit);

            txtDescription.setTextColor(Color.BLUE);
        }
    }
}
