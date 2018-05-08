package com.stfalcon.chatkit.messages.messagetypes;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.stfalcon.chatkit.messages.ConversationMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 07/05/18.
 */

public class DocumentMessageFactory extends MessageFactory<DocumentMessageFactory.DocumentMessageHolder> {

    private final Context context;

    public DocumentMessageFactory(Context context) {
        this.context = context;
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
    public void bindMessageHolder(DocumentMessageHolder messageHolder, Message message) {
        messageHolder.documentImage.setTag(message);
        messageHolder.documentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDocumentClick(v);
            }
        });
    }

    public static class DocumentMessageHolder extends MessageFactory.MessageHolder {
        ImageView documentImage;

        public DocumentMessageHolder(View view) {
            documentImage = view.findViewById(R.id.iv_document);
        }

    }

    protected void onDocumentClick(View v) {
        if(v.getTag() != null && v.getTag() instanceof Message) {
            final Message message = (Message) v.getTag();
            new Thread(new Runnable() {
                public void run() {
                    Uri path = FileProvider.getUriForFile(context,
                            "io.chatcamp.app.fileprovider",
                            downloadFile(message.getAttachment().getUrl()));
//                Uri path = Uri.fromFile(downloadFile(message.getDocumentUrl()));
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        if (message instanceof ConversationMessage) {
                            intent.setDataAndType(path, message.getAttachment().getType());
//                        } else {
//                            intent.setDataAndType(path, "application/*");
//                        }
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            }).start();
        }
    }

    private File downloadFile(String downloadFilePath) {

        File file = null;
        try {
            File SDCardRoot = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File serverFile = new File(downloadFilePath);
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, serverFile.getName());
            if (file.exists()) {
                return file;
            }

            URL url = new URL(downloadFilePath);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
//
            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            int totalsize = urlConnection.getContentLength();
            int downloadedSize = 0;

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                final float per = ((float) downloadedSize / totalsize) * 100;
                //TODO we should not refer activity from here
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        progressBar.setVisibility(View.VISIBLE);
//                        progressBar.setProgress((int) per);
                    }
                });

            }
            //TODO we should not refer activity from here
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    progressBar.setVisibility(View.GONE);
//                    progressBar.setProgress(0);
                }
            });

            // close the output stream when complete //
            fileOutput.close();

        } catch (final MalformedURLException e) {
            Log.e("document", e.getMessage());
        } catch (final IOException e) {
            Log.e("document", e.getMessage());
        } catch (final Exception e) {
            Log.e("document", e.getMessage());
        }
        return file;
    }
}
