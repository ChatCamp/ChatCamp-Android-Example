package com.stfalcon.chatkit.messages.messagetypes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stfalcon.chatkit.R;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 04/05/18.
 */

public class VideoMessageFactory extends MessageFactory<VideoMessageFactory.VideoMessageHolder> {

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
    public void bindMessageHolder(VideoMessageHolder messageHolder, Message message) {

    }

    public static class VideoMessageHolder extends MessageFactory.MessageHolder {

        ImageView videoImage;

        public VideoMessageHolder(View view) {
            videoImage = view.findViewById(R.id.iv_video);
        }

    }
}
