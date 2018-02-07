package io.chatcamp.app;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import io.chatcamp.sdk.Message;
import java.util.Date;

/**
 * Created by ChatCamp Team on 05/02/18.
 */

public class ConversationMessage implements MessageContentType, MessageContentType.Image {

    private Message message;
    private ConversationAuthor author;

    public ConversationMessage(Message message) {
        this.message = message;
        this.author = new ConversationAuthor(this.message.getUser());
    }

    @Override
    public String getId() {
        return message.getText();
    }

    @Override
    public String getText() {
        if(message.getType().equals("text")) {
            return message.getText();
        }
        else if(message.getType().equals("attachment")) {
            return message.getAttachment().getUrl();
        }
        return null;
    }

    @Override
    public ConversationAuthor getUser() {
        return this.author;
    }

    @Override
    public Date getCreatedAt() {
        long ms = this.message.getInsertedAt();
        Date date = new Date(ms);
        return date;
    }

    @Override
    public String getImageUrl() {
        if(message.getType().equals("attachment")) {
            System.out.println("URL" + message.getAttachment().getUrl());
        }
        return message.getType().equals("attachment") && message.getAttachment().isImage() ? message.getAttachment().getUrl() : null;
    }

    public Message getMessage() {
        return message;
    }

}
