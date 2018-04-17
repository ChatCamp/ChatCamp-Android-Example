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
public class TextSender {

    protected BaseChannel channel;

    public TextSender(@NonNull BaseChannel channel) {
        this.channel = channel;
    }

    public  void sendMessage(@NonNull String message) {
        channel.sendMessage(message, new BaseChannel.SendMessageListener() {
            @Override
            public void onSent(Message message, ChatCampException e) {
                // can do something here
            }
        });
    }
}
