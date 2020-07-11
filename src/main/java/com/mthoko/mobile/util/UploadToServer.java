package com.mthoko.mobile.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mthoko.mobile.R;

import java.io.IOException;

import static com.mthoko.mobile.service.common.RecordingService.EXTENSION;
import static com.mthoko.mobile.service.common.RecordingService.RECORDING_DIRECTORY;

public class UploadToServer extends Activity {

    TextView txtMessage;
    EditText txtUrl;
    Button uploadButton;

    //String upLoadServerUri = null;

    /**********  File Path *************/
    final String UPLOAD_FILE_PATH = RECORDING_DIRECTORY + "/";
    final String UPLOAD_FILE_NAME = "recording" + EXTENSION;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);

        uploadButton = findViewById(R.id.uploadButton);
        txtMessage = findViewById(R.id.txtMessage);
        txtUrl = findViewById(R.id.txtUrl);
        final String filename = UPLOAD_FILE_PATH + UPLOAD_FILE_NAME;
        txtMessage.setText("Uploading file path :- '" + filename);

        /************* Php script path ****************/
        //upLoadServerUri = "http://192.168.100.104:8080/target/UploadServlet";
        //upLoadServerUri = "http://192.168.56.1:8080/target/UploadFile.htm";
        final String upLoadServerUri = txtUrl.getText().toString();

        uploadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtMessage.setText("uploading started.....");
                            }
                        });
                        try {
                            DataManager.uploadFile(filename, upLoadServerUri);
                        } catch (IOException e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    txtMessage.setText("uploading failed");
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                txtMessage.setText("uploading complete");
                            }
                        });

                    }
                }).start();
            }
        });
    }
}