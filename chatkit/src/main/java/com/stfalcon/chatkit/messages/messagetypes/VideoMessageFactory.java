package com.stfalcon.chatkit.messages.messagetypes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.preview.ShowVideoActivity;
import com.stfalcon.chatkit.utils.DownloadFileListener;
import com.stfalcon.chatkit.utils.FileUtils;

import java.lang.ref.WeakReference;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 04/05/18.
 */

public class VideoMessageFactory extends MessageFactory<VideoMessageFactory.VideoMessageHolder> {
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA = 106;

    private final WeakReference<Activity> activityWeakReference;
    private View view;
    private ProgressBar progressBar;
    private ImageView downloadIcon;
    private Handler handler;

    public VideoMessageFactory(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
        handler = new Handler();
    }

    @Override
    public boolean isBindable(Message message) {
        if (message.getType().equals("attachment")) {
            if (message.getAttachment().isVideo()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public VideoMessageHolder createMessageHolder(ViewGroup cellView,
                                                  boolean isMe, LayoutInflater layoutInflater) {
        return new VideoMessageHolder(layoutInflater.inflate(R.layout.layout_message_video, cellView, true));
    }

    @Override
    public void bindMessageHolder(final VideoMessageHolder messageHolder, final Message message) {
        final Activity activity = activityWeakReference.get();
        if(activity == null) {
            messageHolder.downloadIcon.setVisibility(View.GONE);
            return;
        }
        messageHolder.videoImage.setTag(message);
        messageHolder.videoName.setText(message.getAttachment().getName());
        messageHolder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null && v.getTag() instanceof Message) {
                    if (!FileUtils.fileExists(activity, message.getAttachment().getUrl(), Environment.DIRECTORY_MOVIES)) {
                        messageHolder.progressBar.setVisibility(View.VISIBLE);
                        messageHolder.progressBar.setProgress(0);
                    } else {
                        messageHolder.progressBar.setVisibility(View.GONE);
                    }
                    onVideoClick(v, messageHolder.progressBar, messageHolder.downloadIcon);
                }
            }
        });
        if (FileUtils.fileExists(activity, message.getAttachment().getUrl(), Environment.DIRECTORY_MOVIES)) {
            messageHolder.downloadIcon.setVisibility(View.GONE);
        } else {
            messageHolder.downloadIcon.setVisibility(View.VISIBLE);
        }
    }


    private void onVideoClick(View v, ProgressBar progressBar, ImageView downloadIcon) {
        Activity activity = activityWeakReference.get();

        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            view = v;
            this.progressBar = progressBar;
            this.downloadIcon = downloadIcon;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA);

        } else {
            downloadVideo(v, progressBar, downloadIcon, activity);
        }
    }

    protected void downloadVideo(View v, final ProgressBar progressBar, final ImageView downloadIcon,
                                 final Activity activity) {
        if (activity == null) {
            return;
        }
        if (v.getTag() != null && v.getTag() instanceof Message) {
            final Message message = (Message) v.getTag();
            final String imageUrl = message.getAttachment().getUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                new Thread(new Runnable() {
                    public void run() {
                        Uri path = null;
                        try {
                            path = FileProvider.getUriForFile(activity,
                                    activity.getPackageName() + ".chatcamp.fileprovider",
                                    FileUtils.downloadFile(activity, imageUrl,
                                            Environment.DIRECTORY_MOVIES, new DownloadFileListener() {
                                                @Override
                                                public void downloadProgress(final int progress) {
                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (progressBar != null) {
                                                                progressBar.setProgress(progress);
                                                            }
                                                            if (downloadIcon != null) {
                                                                downloadIcon.setVisibility(View.GONE);
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
                            Intent intent = new Intent(activity, ShowVideoActivity.class);
                            intent.putExtra(ShowVideoActivity.VIDEO_URL, finalPath.toString());
                            activity.startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadVideo(view, progressBar, downloadIcon, activityWeakReference.get());
            }
        }
    }

    public static class VideoMessageHolder extends MessageFactory.MessageHolder {
        ImageView videoImage;
        ProgressBar progressBar;
        ImageView downloadIcon;
        TextView videoName;

        public VideoMessageHolder(View view) {
            videoImage = view.findViewById(R.id.iv_video);
            progressBar = view.findViewById(R.id.progress_bar);
            downloadIcon = view.findViewById(R.id.iv_download);
            videoName = view.findViewById(R.id.tv_video_name);
        }

    }
}
