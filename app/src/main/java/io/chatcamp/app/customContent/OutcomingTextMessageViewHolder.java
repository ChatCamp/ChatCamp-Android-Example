package io.chatcamp.app.customContent;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;

import io.chatcamp.app.ConversationMessage;
import io.chatcamp.app.R;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class OutcomingTextMessageViewHolder extends MessageHolders.OutcomingTextMessageViewHolder<ConversationMessage> {

    TextView usernameTv;
    ImageView avatarIv;
    TextView timeTv;

    public OutcomingTextMessageViewHolder(View itemView) {
        super(itemView);
        usernameTv = itemView.findViewById(R.id.tv_username);
        avatarIv = itemView.findViewById(R.id.messageUserAvatar);
        timeTv = itemView.findViewById(R.id.tv_message_time);
    }

    @Override
    public void onBind(ConversationMessage message) {
        super.onBind(message);
        usernameTv.setText(message.getUser().getName());
        timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        Picasso.with(itemView.getContext()).load(message.getUser().getAvatar()).into(avatarIv);
    }
}
