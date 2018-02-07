package io.chatcamp.app.customContent;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.util.Map;

import io.chatcamp.app.ConversationMessage;
import io.chatcamp.app.R;

/**
 * Created by shubhamdhabhai on 07/02/18.
 */

public class IncomingActionMessageViewHolder extends IncomingTextMessageViewHolder {
    private final TextView actionTitle;
    private final TextView actionCode;
    private final TextView actionDescription;
    private final TextView actionShippingCost;
    private final ImageView actionImage;
    private final TextView actionText;
    private Gson gson;

    public IncomingActionMessageViewHolder(View itemView) {
        super(itemView);
        actionImage = itemView.findViewById(R.id.iv_action_image);
        actionText = itemView.findViewById(R.id.messageText);
        actionTitle = itemView.findViewById(R.id.tv_action_title);
        actionCode = itemView.findViewById(R.id.tv_action_code);
        actionDescription = itemView.findViewById(R.id.tv_action_description);
        actionShippingCost = itemView.findViewById(R.id.tv_action_shipping_cost);
        gson = new Gson();
    }

    @Override
    public void onBind(ConversationMessage message) {
        super.onBind(message);
        Map<String, String> map = message.getMessage().getMetadata();
        String product = map.get("product");
        ActionMessage actionMessage = gson.fromJson(product, ActionMessage.class);
        String completeImageUrl = actionMessage.getImageURL();
        String imageUrl;
        if(!completeImageUrl.contains("http")) {
            imageUrl = "http://" + completeImageUrl.substring(2, completeImageUrl.length() - 2);
        } else {
            imageUrl = completeImageUrl.substring(2, completeImageUrl.length() - 2);
        }
        Picasso.with(itemView.getContext()).load(imageUrl).into(actionImage);
        actionTitle.setText(actionMessage.getName());
        actionCode.setText(String.format("Code: %s", actionMessage.getCode()));
        actionDescription.setText(actionMessage.getShortDescription());
        actionShippingCost.setText(String.format("₹ %d shipping cost", actionMessage.getShippingCost()));
        if (TextUtils.isEmpty(message.getText())) {
            actionText.setVisibility(View.GONE);
        } else {
            actionText.setText(message.getText());
        }
    }
}
