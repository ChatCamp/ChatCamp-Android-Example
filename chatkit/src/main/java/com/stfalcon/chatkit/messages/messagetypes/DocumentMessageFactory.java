package com.stfalcon.chatkit.messages.messagetypes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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

import java.lang.ref.WeakReference;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 07/05/18.
 */


public class DocumentMessageFactory extends MessageFactory<DocumentMessageFactory.DocumentMessageHolder> {

    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA = 104;
    private WeakReference<Activity> activityWeakReference;
    private Handler handler;
    private View view;
    private TextView textView;

    public DocumentMessageFactory(Activity activity) {
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
        messageHolder.documentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDocumentClick(v, messageHolder.textView);
            }
        });
    }

    private void onDocumentClick(View v, TextView textView) {
        Activity activity = activityWeakReference.get();

        if (activity == null) {
            return;
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
        view = v;
        this.textView = textView;
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA);
//
//        } else {
            downloadDocument(v, textView, activity);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (activityWeakReference.get() != null) {
                    downloadDocument(view, textView,activityWeakReference.get());
                }
            }
        }
    }

    public static class DocumentMessageHolder extends MessageFactory.MessageHolder {
        ImageView documentImage;
        ProgressBar progressBar;
        TextView textView;

        public DocumentMessageHolder(View view) {
            documentImage = view.findViewById(R.id.iv_document);
            progressBar = view.findViewById(R.id.progress_bar);
            textView = view.findViewById(R.id.text);
        }

    }

    protected void downloadDocument(View v, final TextView progressBar, final Activity activity) {
        if (v.getTag() != null && v.getTag() instanceof Message) {
            final Message message = (Message) v.getTag();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Uri path = FileProvider.getUriForFile(activity,
                                activity.getPackageName() + ".chatcamp.fileprovider",
                                FileUtils.downloadFile(message.getAttachment().getUrl(),
                                        Environment.DIRECTORY_DOWNLOADS, new DownloadFileListener() {
                                            @Override
                                            public void downloadProgress(final int progress) {
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
//                                                    progressBar.setProgress(progress);
                                                        progressBar.setText(String.valueOf(progress));
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

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(path, message.getAttachment().getType());
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
