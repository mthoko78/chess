package com.mthoko.mobile.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mthoko.mobile.R;
import com.mthoko.mobile.exception.ApplicationException;
import com.mthoko.mobile.service.FTPService;
import com.mthoko.mobile.service.common.ServiceFactory;

import org.apache.commons.net.ftp.FTPFile;

public class ExportFragment extends BaseFragment<ExportFragment> {

    EditText txtUrl;
    Button uploadButton;

    public ExportFragment() {
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
        return inflater.inflate(R.layout.fragment_export, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadButton = view.findViewById(R.id.uploadButton);
        txtUrl = view.findViewById(R.id.txtUrl);
        final String upLoadServerUri = txtUrl.getText().toString();
        Log.i("URL", upLoadServerUri);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        FTPService ftpService = ServiceFactory.getFtpService(getContext());
                        try {
                            FTPFile[] clientFiles = ftpService.getClientFiles();
                        } catch (ApplicationException e) {
                            e.printStackTrace();
                        }
                        Log.e("action", "upload complete");
                    }
                }.start();
            }
        });
    }

}
