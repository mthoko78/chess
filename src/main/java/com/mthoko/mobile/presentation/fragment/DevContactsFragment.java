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
import com.mthoko.mobile.entity.DevContact;
import com.mthoko.mobile.entity.DevContactValue;
import com.mthoko.mobile.model.RecyclerItem;
import com.mthoko.mobile.presentation.activity.MyAdapter;
import com.mthoko.mobile.presentation.listener.MenuItemSelector;
import com.mthoko.mobile.service.DevContactService;
import com.mthoko.mobile.service.DeviceService;
import com.mthoko.mobile.service.common.ServiceFactory;

import java.util.List;

import static com.mthoko.mobile.resource.internal.MyResources.showMessage;
import static com.mthoko.mobile.service.internal.DevContactServiceImpl.STRING_TYPE;

public class DevContactsFragment extends BaseFragment<DevContactsFragment> {

    private DeviceService deviceService;
    private DevContactService devContactService;

    public DevContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dev_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceService = ServiceFactory.getDeviceService(view.getContext());
        devContactService = ServiceFactory.getContactService(view.getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //listItems.clear();
        try {
            initItems();
            adapter = new MyAdapter(listItems, view.getContext(), R.menu.contact_menu, new MenuItemSelector() {
                @Override
                public void onItemSelect(MenuItem item, int position) {
                    switch (item.getItemId()) {
                        case R.id.mnu_item_call:
                            Toast.makeText(adapter.getContext(), "Unable to make call please try again later", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.mnu_item_delete:
                            RecyclerItem remove = adapter.getListItems().remove(position);
                            DevContact devContact = devContactService.findById(remove.getId());
                            devContactService.delete(devContact);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(adapter.getContext(), "Deleted: " + devContact.getName(), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            break;
                    }
                }
            });
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(DevContactsFragment.this.getContext(), e.getMessage());
        }
    }

    private void initItems() {
        listItems.clear();
        List<DevContact> contacts = devContactService.findByImei(deviceService.getImei());
        for (DevContact contact : contacts) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                for (DevContactValue contactValue : contact.getValues()) {
                    String s = STRING_TYPE[contactValue.getSource()] + " : " + contactValue.getValue() + "\n";
                    stringBuilder.append(s);
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                RecyclerItem item = new RecyclerItem(contact.getName(), stringBuilder.toString(), contact.getId());
                listItems.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
