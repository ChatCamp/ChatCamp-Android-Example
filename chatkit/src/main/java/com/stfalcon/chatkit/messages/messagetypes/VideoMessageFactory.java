package com.stfalcon.chatkit.messages.messagetypes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.preview.ShowImageActivity;
import com.stfalcon.chatkit.preview.ShowVideoActivity;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 04/05/18.
 */

public class VideoMessageFactory extends MessageFactory<VideoMessageFactory.VideoMessageHolder> {

    private final Context context;

    public VideoMessageFactory(Context context) {
        this.context = context;
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
    public void bindMessageHolder(VideoMessageHolder messageHolder, Message message) {
        messageHolder.videoImage.setTag(message);
        messageHolder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag() != null && v.getTag() instanceof Message) {
                    Message clickedMessage = (Message) v.getTag();
                    Intent intent = new Intent(context, ShowVideoActivity.class);
                    intent.putExtra(ShowVideoActivity.VIDEO_URL, clickedMessage.getAttachment().getUrl());
                    context.startActivity(intent);
                }
            }
        });
    }

    public static class VideoMessageHolder extends MessageFactory.MessageHolder {

        ImageView videoImage;

        public VideoMessageHolder(View view) {
            videoImage = view.findViewById(R.id.iv_video);
        }

    }
}
