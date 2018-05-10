package com.stfalcon.chatkit.preview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.utils.DownloadFileListener;
import com.stfalcon.chatkit.utils.FileUtils;

public class ShowVideoActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA = 105;
    private Handler handler;
    public static final String VIDEO_URL = "video_url";
    private ProgressBar progressBar;
    private String videoUrl;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        handler = new Handler();
        videoView = findViewById(R.id.vv_video);
        if (getIntent().hasExtra(VIDEO_URL)) {
            videoUrl = getIntent().getStringExtra(VIDEO_URL);
        }
        progressBar = findViewById(R.id.progress_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA);

        } else {
            downloadVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadVideo();
            }
        }
    }

    protected void downloadVideo() {
        if(FileUtils.fileExists(this, videoUrl, Environment.DIRECTORY_MOVIES)) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(videoUrl)) {
            new Thread(new Runnable() {
                public void run() {
                    Uri path = null;
                    try {
                        path = FileProvider.getUriForFile(ShowVideoActivity.this,
                                ShowVideoActivity.this.getPackageName() + ".chatcamp.fileprovider",
                                FileUtils.downloadFile(ShowVideoActivity.this, videoUrl,
                                        Environment.DIRECTORY_MOVIES, new DownloadFileListener() {
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
                                videoView.setVideoURI(finalPath);
                                MediaController mediaController = new MediaController(ShowVideoActivity.this);
                                videoView.setMediaController(mediaController);
                                videoView.start();
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
