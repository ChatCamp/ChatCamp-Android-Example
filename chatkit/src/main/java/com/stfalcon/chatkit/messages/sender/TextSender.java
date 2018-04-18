package com.stfalcon.chatkit.messages.sender;

import android.support.annotation.NonNull;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 18/04/18.
 *
 * Extend this class and override the send method to customize the class
 */
public abstract class TextSender {

    protected BaseChannel channel;

    public TextSender(@NonNull BaseChannel channel) {
        this.channel = channel;
    }

    public abstract void sendMessage(@NonNull String message);
}
