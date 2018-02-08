package io.chatcamp.app.customContent;

import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.utils.DateFormatter;
import com.stfalcon.chatkit.utils.RoundedImageView;

import io.chatcamp.app.ConversationMessage;
import io.chatcamp.app.R;

/**
 * Created by shubhamdhabhai on 08/02/18.
 */

public class IncomingImageMessageViewHolder extends MessageHolders.BaseIncomingMessageViewHolder<ConversationMessage> {
    TextView timeTv;
    TextView usernameTv;
    RoundedImageView roundedImageView;

    public IncomingImageMessageViewHolder(View itemView) {
        super(itemView);
        usernameTv = itemView.findViewById(R.id.tv_username);
        timeTv = itemView.findViewById(R.id.tv_message_time);
        roundedImageView = itemView.findViewById(R.id.image);
        if (roundedImageView != null && roundedImageView instanceof RoundedImageView) {
            ((RoundedImageView) roundedImageView).setCorners(
                    com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
                    com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius,
                    0,
                    com.stfalcon.chatkit.R.dimen.message_bubble_corners_radius
            );
        }
    }

    @Override
    public void onBind(ConversationMessage message) {
        super.onBind(message);
        timeTv.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
        usernameTv.setText(message.getUser().getName());
        Picasso.with(itemView.getContext()).load(message.getImageUrl()).into(roundedImageView);
    }
}
