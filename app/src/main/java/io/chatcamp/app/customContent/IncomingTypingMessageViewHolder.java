package io.chatcamp.app.customContent;

import android.view.View;
import android.widget.TextView;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.wang.avi.AVLoadingIndicatorView;

import io.chatcamp.app.ConversationMessage;
import io.chatcamp.app.R;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class IncomingTypingMessageViewHolder extends MessageHolders.IncomingTextMessageViewHolder<ConversationMessage> {

    TextView usernameTv;
    AVLoadingIndicatorView indicator;

    public IncomingTypingMessageViewHolder(View itemView) {
        super(itemView);
        usernameTv = itemView.findViewById(R.id.tv_username);
        indicator = itemView.findViewById(R.id.indication);
    }

    @Override
    public void onBind(ConversationMessage message) {
        super.onBind(message);
        usernameTv.setText(message.getUser().getName());
        indicator.show();
    }
}
