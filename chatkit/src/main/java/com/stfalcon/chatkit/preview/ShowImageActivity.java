package com.stfalcon.chatkit.preview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.utils.DownloadFileListener;
import com.stfalcon.chatkit.utils.FileUtils;

public class ShowImageActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA = 105;
    private Handler handler;
    public static final String IMAGE_URL = "image_url";
    private ProgressBar progressBar;
    private String imageUrl;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        handler = new Handler();
        imageView = findViewById(R.id.iv_image);
        if (getIntent().hasExtra(IMAGE_URL)) {
            imageUrl = getIntent().getStringExtra(IMAGE_URL);
        }
        progressBar = findViewById(R.id.progress_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA);

        } else {
            downloadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadImage();
            }
        }
    }

    protected void downloadImage() {
        if(FileUtils.fileExists(this, imageUrl, Environment.DIRECTORY_PICTURES)) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            new Thread(new Runnable() {
                public void run() {
                    Uri path = null;
                    try {
                        path = FileProvider.getUriForFile(ShowImageActivity.this,
                                ShowImageActivity.this.getPackageName() + ".chatcamp.fileprovider",
                                FileUtils.downloadFile(ShowImageActivity.this, imageUrl,
                                        Environment.DIRECTORY_PICTURES, new DownloadFileListener() {
                                            @Override
                                            public void downloadProgress(final int progress) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (progressBar != null) {
                                                            progressBar.setProgress(progress);
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void downloadComplete() {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressBar.setVisibility(View.GONE);

                                                    }
                                                });
                                            }
                                        }));

                        final Uri finalPath = path;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.with(ShowImageActivity.this).load(finalPath).into(imageView);
                            }
                        });
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
