package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mthoko.mobile.R;
import com.mthoko.mobile.entity.Sms;
import com.mthoko.mobile.service.common.ServiceFactory;
import com.mthoko.mobile.service.SmsService;

import java.util.List;

import static com.mthoko.mobile.presentation.activity.DrawerActivity.INSTANCE;

public class ImportFragment extends BaseFragment<ImportFragment> {

    private TextView textView;
    private Button button;
    private SmsService smsService;


    public ImportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toast(String s) {
        Toast.makeText(this.getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = INSTANCE.findViewById(R.id.button);
        textView = INSTANCE.findViewById(R.id.textView);
        smsService = ServiceFactory.getSmsService(view.getContext());
        button.setText("Sorted now");
        List<Sms> smses = smsService.getActualInbox();
        textView.setText("INBOX: \n\n");
        for (Sms sms : smses) {
            textView.append(sms.getFormattedString() + "\n\n");
        }
    }
}
