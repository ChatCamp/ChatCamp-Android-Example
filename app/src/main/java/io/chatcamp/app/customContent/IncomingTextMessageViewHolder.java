package io.chatcamp.app.customContent;

import android.view.View;
import android.widget.TextView;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

import io.chatcamp.app.ConversationMessage;
import io.chatcamp.app.R;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class IncomingTextMessageViewHolder
        extends MessageHolders.IncomingTextMessageViewHolder<ConversationMessage> {

    TextView usernameTv;
    TextView timeTv;

    public IncomingTextMessageViewHolder(View itemView) {
        super(itemView);
        usernameTv = itemView.findViewById(R.id.tv_username);
        timeTv = itemView.findViewById(R.id.tv_message_time);
    }

    @Override
    public void onBind(ConversationMessage message) {
        super.onBind(message);
        timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        usernameTv.setText(message.getUser().getName());
    }
}
