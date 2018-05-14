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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.utils.DownloadFileListener;
import com.stfalcon.chatkit.utils.FileUtils;

import java.io.File;
import java.lang.ref.WeakReference;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 07/05/18.
 */


public class FileMessageFactory extends MessageFactory<FileMessageFactory.DocumentMessageHolder> {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA = 104;
    private WeakReference<Activity> activityWeakReference;
    private Handler handler;
    private View view;
    private ProgressBar progressBar;
    private ImageView downloadIcon;

    public FileMessageFactory(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
        handler = new Handler();

    }

    @Override
    public boolean isBindable(Message message) {
        if (message.getType().equals("attachment")) {
            if (message.getAttachment().isDocument()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DocumentMessageHolder createMessageHolder(ViewGroup cellView, boolean isMe, LayoutInflater layoutInflater) {
        return new DocumentMessageHolder(layoutInflater.inflate(R.layout.layout_message_document, cellView, true));
    }

    @Override
    public void bindMessageHolder(final DocumentMessageHolder messageHolder, final Message message) {
        messageHolder.documentImage.setTag(message);
        messageHolder.documentName.setText(message.getAttachment().getName());
        final Activity activity = activityWeakReference.get();
        if (activity == null) {
            messageHolder.downloadIcon.setVisibility(View.GONE);
            return;
        }
        messageHolder.documentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FileUtils.fileExists(activity, message.getAttachment().getUrl(), Environment.DIRECTORY_DOWNLOADS)) {
                    messageHolder.progressBar.setVisibility(View.VISIBLE);
                    messageHolder.progressBar.setProgress(0);
                } else {
                    messageHolder.progressBar.setVisibility(View.GONE);
                }
                onDocumentClick(v, messageHolder.progressBar, messageHolder.downloadIcon);
            }
        });

        if (FileUtils.fileExists(activity, message.getAttachment().getUrl(), Environment.DIRECTORY_DOWNLOADS)) {
            messageHolder.downloadIcon.setVisibility(View.GONE);
        } else {
            messageHolder.downloadIcon.setVisibility(View.VISIBLE);
        }
    }

    private void onDocumentClick(View v, ProgressBar progressBar, ImageView downloadIcon) {
        Activity activity = activityWeakReference.get();

        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            view = v;
            this.progressBar = progressBar;
            this.downloadIcon = downloadIcon;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA);

        } else {
            downloadDocument(v, progressBar, downloadIcon, activity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (activityWeakReference.get() != null) {
                    if (view != null && progressBar != null) {
                        downloadDocument(view, progressBar, downloadIcon, activityWeakReference.get());
                    }
                }
            }
        }
    }

    public static class DocumentMessageHolder extends MessageFactory.MessageHolder {
        ImageView documentImage;
        ProgressBar progressBar;
        ImageView downloadIcon;
        TextView documentName;

        public DocumentMessageHolder(View view) {
            documentImage = view.findViewById(R.id.iv_document);
            progressBar = view.findViewById(R.id.progress_bar);
            downloadIcon = view.findViewById(R.id.iv_download);
            documentName = view.findViewById(R.id.tv_document_name);
        }

    }

    protected void downloadDocument(View v, final ProgressBar progressBar, final ImageView downloadIcon,
                                    final Activity activity) {
        if (v.getTag() != null && v.getTag() instanceof Message) {
            final Message message = (Message) v.getTag();
            new Thread(new Runnable() {
                public void run() {
                    Uri path = null;
                    try {
                       File file =  FileUtils.downloadFile(activity, message.getAttachment().getUrl(),
                                Environment.DIRECTORY_DOWNLOADS, new DownloadFileListener() {
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
                                });
                       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                           path = FileProvider.getUriForFile(activity,
                                   activity.getPackageName() + ".chatcamp.fileprovider", file
                           );
                       } else {
                           path = Uri.fromFile(file);
                       }

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, message.getAttachment().getType());
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (path != null) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(path, "application/*");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                activity.startActivity(intent);
                            } catch (Exception error) {
                                error.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }
}
