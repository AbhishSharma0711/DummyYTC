package com.sap.ytconverter.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoDownloaderTask extends AsyncTask<String, String, Boolean> {
    public VideoDownloaderTask(Context context) {
        this.context = context;
    }
    private ProgressDialog progressDialog;
    private Context context;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(context, null, "Please Wait ...", true);
        //progressDialog.setCancelable(true);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String fileName = extractFileNameFromUrl(url);
        downloadFile(url, fileName);

        convertNow("-Version" + " " + context.getFilesDir().getAbsolutePath()
                + File.separator + "Video" + File.separator + fileName + " " + Environment.getExternalStorageDirectory()
                + File.separator + "Audio" + File.separator + fileName + ".mp3");
        return true;
    }

    private String extractFileNameFromUrl(String url) {
        /*String reverseTempVariable = "";

        for (int i = url.length()-1; i>=0 ; i--) {
            reverseTempVariable += url.charAt(i);
        }

        return reverseTempVariable.split(".")[1].split("/")[0];*/
        return url.split("=")[1];
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        //progressDialog.dismiss();
        if (s) {
            Toast.makeText(context, "Download Success, now converting...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Unable to download. Please Try Again...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean downloadFile(String fileURL, String fileName) {
        try {
            String rootDir = context.getFilesDir().getAbsolutePath()
                    + File.separator + "Video";
            File rootFile = new File(rootDir);
            rootFile.mkdir();
            URL url = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(rootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
        } catch (IOException e) {
            Log.d("Error....", e.toString());
            return false;
        }

        return true;
    }

    public void convertNow(String... cmd) {
        FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {}

                @Override
                public void onSuccess(String message) {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }
}
