package com.stfalcon.chatkit.messages.messagetypes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.stfalcon.chatkit.R;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 07/05/18.
 */

public class DocumentMessageFactory extends MessageFactory<DocumentMessageFactory.DocumentMessageHolder> {

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

    }

    public static class DocumentMessageHolder extends MessageFactory.MessageHolder {
        ImageView documentImage;

        public DocumentMessageHolder(View view) {
            documentImage = view.findViewById(R.id.iv_document);
        }

    }
}
