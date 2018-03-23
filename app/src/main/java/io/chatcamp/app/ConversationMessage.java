package io.chatcamp.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.stfalcon.chatkit.commons.models.IActionContent;
import com.stfalcon.chatkit.commons.models.IActionMessage;
import com.stfalcon.chatkit.commons.models.IActionSubContent;
import com.stfalcon.chatkit.commons.models.MessageContentType;
import com.stfalcon.chatkit.messages.MessageType;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import io.chatcamp.app.customContent.ActionContent;
import io.chatcamp.app.customContent.ActionMessage;
import io.chatcamp.app.customContent.ActionSubContent;
import io.chatcamp.sdk.Message;

/**
 * Created by ChatCamp Team on 05/02/18.
 */

public class ConversationMessage implements MessageContentType,
        MessageContentType.Image, MessageContentType.Video, MessageContentType.Document {

    public static final String TYPING_TEXT_ID = "chatcamp_typing_id";


    private Message message;
    private ConversationAuthor author;
    private Gson gson = new Gson();

    //TODO remove id, this is only for testing indicator while typing, in real the id should come from server
    private String id;

    public ConversationMessage() {
    }

    public ConversationMessage(Message message) {
        this.message = message;
        this.author = new ConversationAuthor(this.message.getUser());
        this.id = message.getId();
    }

    public ConversationMessage(ConversationMessage conversationMessage) {
        this.message = conversationMessage.getMessage();
        this.author = new ConversationAuthor(this.message.getUser());
        this.id = conversationMessage.getId();
    }

    @Override
    public String getId() {
        //TODO uncomment this, it is only for testing indicator, in real the id should come from server
//        return message.getText();
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor(ConversationAuthor author) {
        this.author = author;
    }

    @Override
    public String getText() {
        if (message.getType().equals("text")) {
            return message.getText();
        } else if (message.getType().equals("attachment")) {
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
        long ms = Calendar.getInstance().getTimeInMillis();
        if (message != null) {
            ms = this.message.getInsertedAt() * 1000;
        }
        Date date = new Date(ms);
        return date;
    }

    @Override
    public int getMessageType() {
        if (id.contains(TYPING_TEXT_ID)) {
            return MessageType.VIEW_TYPE_TYPING_MESSAGE_CHAT_CAMP;
        }
        else if (message.getType().equals("attachment")) {
            if(message.getAttachment().isImage()) {
                return MessageType.VIEW_TYPE_IMAGE_MESSAGE_CHATCAMP;
            } else if(message.getAttachment().isVideo()){
                return MessageType.VIEW_TYPE_VIDEO_MESSAGE_CHATCAMP;
            }
            else if(message.getAttachment().isDocument()) {
                return MessageType.VIEW_TYPE_DOCUMENT_MESSAGE_CHATCAMP;
            } else {
               return MessageType.VIEW_TYPE_TEXT_MESSAGE_CHATCAMP;
            }
        } else if (message.getType().equals("text")
                && message.getCustomType().equals("action_link")) {
            return MessageType.VIEW_TYPE_ACTION_MESSAGE_CHATCAMP;
        } else if (message.getType().equals("text")) {
            return MessageType.VIEW_TYPE_TEXT_MESSAGE_CHATCAMP;
        }
        return MessageType.VIEW_TYPE_TEXT_MESSAGE_CHATCAMP;
    }

    public String getImageUrl() {
        if (message.getType().equals("attachment")) {
            System.out.println("URL" + message.getAttachment().getUrl());
        }
        return message.getType().equals("attachment") && message.getAttachment().isImage() ? message.getAttachment().getUrl() : null;
    }

    public String getVideoUrl() {
        if (message.getType().equals("attachment")) {
            System.out.println("URL" + message.getAttachment().getUrl());
        }
        return message.getType().equals("attachment") && message.getAttachment().isVideo() ? message.getAttachment().getUrl() : null;
    }

    public String getDocumentUrl() {
        if (message.getType().equals("attachment")) {
            System.out.println("URL" + message.getAttachment().getUrl());
        }
        return message.getType().equals("attachment") && message.getAttachment().isDocument() ? message.getAttachment().getUrl() : null;
    }

    public Message getMessage() {
        return message;
    }

    public IActionMessage getActionMessage() {
        Map<String, String> map = message.getMetadata();
        String product = map.get("product");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IActionContent.class, new IActionContentAdapter<>());
        builder.registerTypeAdapter(IActionSubContent.class, new IActionSubContentAdapter<>());
        ActionMessage actionMessage = builder.create().fromJson(product, ActionMessage.class);
        return actionMessage;
    }

    public String getFileName() {
        if(message.getAttachment() != null) {
            return message.getAttachment().getName();
        }
        return null;
    }

    public class IActionContentAdapter<T> implements JsonSerializer<T>, JsonDeserializer {

        private static final String CLASSNAME = "CLASSNAME";
        private static final String DATA = "DATA";

        public T deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return jsonDeserializationContext.deserialize(jsonElement, ActionContent.class);
        }
        public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
            jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
            return jsonObject;
        }
    }

    public class IActionSubContentAdapter<T> implements JsonSerializer<T>, JsonDeserializer {

        private static final String CLASSNAME = "CLASSNAME";
        private static final String DATA = "DATA";

        public T deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return jsonDeserializationContext.deserialize(jsonElement, ActionSubContent.class);
        }
        public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
            jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
            return jsonObject;
        }
    }

}
