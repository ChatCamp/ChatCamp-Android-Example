package com.stfalcon.chatkit.messages.sender;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.File;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.GroupChannel;

/**
 * Created by shubhamdhabhai on 18/04/18.
 */

public abstract class AttachmentSender {

    protected BaseChannel channel;
    @DrawableRes
    //TODO set a default resource here
    protected int drawableRes;
    protected String title = "Attachment";

    private UploadListener uploadListener;

    public interface UploadListener {
        void onUploadProgress(int progress);

        void onUploadSuccess();

        void onUploadFailed();
    }

    public AttachmentSender(@NonNull BaseChannel channel, @NonNull String title, @NonNull @DrawableRes int drawableRes) {
        this.channel = channel;
        this.drawableRes = drawableRes;
        this.title = title;
    }

    public void setUploadListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public String getTitle() {
        return title;
    }

    public void sendAttachment(File file, String fileName, String contentType) {
        channel.sendAttachment(file, fileName, contentType
                , new GroupChannel.UploadAttachmentListener() {
                    @Override
                    public void onUploadProgress(int progress) {
                        if (uploadListener != null) {
                            uploadListener.onUploadProgress(progress);
                        }
                    }

                    @Override
                    public void onUploadSuccess() {
                        if (uploadListener != null) {
                            uploadListener.onUploadSuccess();
                        }
                        //TODO mark read should be a part of BaseChannel and not group channel
                        if (channel instanceof GroupChannel) {
                            ((GroupChannel) channel).markAsRead();
                        }
                    }

                    @Override
                    public void onUploadFailed(Throwable error) {
                        if (uploadListener != null) {
                            uploadListener.onUploadFailed();
                        }
                    }
                });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {}

    public void onActivityResult(int requestCode, int resultCode, Intent dataFile) {}

    public abstract void clickSend();
}
