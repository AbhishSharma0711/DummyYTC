package com.sap.ytconverter.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.sap.ytconverter.R;
import com.sap.ytconverter.utility.VideoDownloaderTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onConvertClick(View view) {
        String url = ((EditText)findViewById(R.id.et_url_link)).getText().toString();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this,"Enter URL please", Toast.LENGTH_SHORT).show();
        } else {
            VideoDownloaderTask task = new VideoDownloaderTask(this);
            task.execute(url);
        }
    }
}
