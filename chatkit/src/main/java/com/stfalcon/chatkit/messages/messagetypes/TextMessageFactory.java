package com.stfalcon.chatkit.messages.messagetypes;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stfalcon.chatkit.R;

import io.chatcamp.sdk.Message;

/**
 * Created by shubhamdhabhai on 21/04/18.
 */

public class TextMessageFactory extends MessageFactory<TextMessageFactory.TextMessageHolder> {

    @Override
    public boolean isBindable(Message message) {
        return message.getType().equals("text");
    }

    @Override
    public TextMessageHolder createMessageHolder(ViewGroup cellView, boolean isMe, LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.layout_message_text, cellView, true);
        TextView textView = view.findViewById(R.id.messageTextView);
        Drawable backgroundDrawable = isMe ? messageStyle.getOutcomingBubbleDrawable() :
                messageStyle.getIncomingBubbleDrawable();
        int paddingTop = isMe ? messageStyle.getOutcomingDefaultBubblePaddingTop() : messageStyle.getIncomingDefaultBubblePaddingTop();
        int paddingBottom = isMe ? messageStyle.getOutcomingDefaultBubblePaddingBottom() : messageStyle.getIncomingDefaultBubblePaddingBottom();
        int paddingLeft = isMe ? messageStyle.getOutcomingDefaultBubblePaddingLeft() : messageStyle.getIncomingDefaultBubblePaddingLeft();
        int paddingRight = isMe ? messageStyle.getOutcomingDefaultBubblePaddingRight() : messageStyle.getIncomingDefaultBubblePaddingRight();
        int textColor = isMe ? messageStyle.getOutcomingTextColor() : messageStyle.getIncomingTextColor();
        int textSize = isMe ? messageStyle.getOutcomingTextSize() : messageStyle.getIncomingTextSize();
        int textStyle = isMe ? messageStyle.getOutcomingTextStyle() : messageStyle.getIncomingTextStyle();
        int textLinkColor = isMe ? messageStyle.getOutcomingTextLinkColor() : messageStyle.getIncomingTextLinkColor();
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setTypeface(textView.getTypeface(), textStyle);
        textView.setAutoLinkMask(messageStyle.getTextAutoLinkMask());
        textView.setLinkTextColor(textLinkColor);
        textView.setTextColor(textColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(backgroundDrawable);
        } else {
            textView.setBackgroundDrawable(backgroundDrawable);
        }
        return new TextMessageHolder(view);
    }

    @Override
    public void bindMessageHolder(TextMessageHolder cellHolder, Message message) {
        cellHolder.messageText.setText(message.getText());
    }

    public static class TextMessageHolder extends MessageFactory.MessageHolder {
        TextView messageText;

        public TextMessageHolder(View view) {
            messageText = view.findViewById(R.id.messageTextView);
        }

    }
}
