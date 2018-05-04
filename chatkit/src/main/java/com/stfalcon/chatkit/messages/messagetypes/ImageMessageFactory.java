package com.stfalcon.chatkit.messages.messagetypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.messages.MessageType;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 03/05/18.
 */

public class ImageMessageFactory extends MessageFactory<ImageMessageFactory.ImageMessageHolder> {

    private final Context context;

    public ImageMessageFactory(Context context) {
        this.context = context;
    }

    @Override
    public boolean isBindable(Message message) {
        if (message.getType().equals("attachment")) {
            if (message.getAttachment().isImage()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ImageMessageHolder createMessageHolder(ViewGroup cellView, boolean isMe, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.layout_message_image, cellView, true);
        return new ImageMessageHolder(view);
    }

    @Override
    public void bindMessageHolder(ImageMessageHolder messageHolder, Message message) {
        Picasso.with(context).load(message.getAttachment().getUrl()).into(messageHolder.imageView);
    }

    public static class ImageMessageHolder extends MessageFactory.MessageHolder {
        ImageView imageView;

        public ImageMessageHolder(View view) {
            imageView = view.findViewById(R.id.iv_message);
        }

    }
}
