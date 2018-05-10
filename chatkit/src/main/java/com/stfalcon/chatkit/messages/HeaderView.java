package com.stfalcon.chatkit.messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.R;
import com.stfalcon.chatkit.conversationdetails.GroupDetailActivity;
import com.stfalcon.chatkit.conversationdetails.UserProfileActivity;

import java.util.List;

import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.Participant;

/**
 * Created by shubhamdhabhai on 10/05/18.
 */

public class HeaderView extends LinearLayout {

    private Toolbar toolbar;
    private ImageView groupImageIv;
    private TextView groupTitleTv;
    private BaseChannel channel;
    private Participant otherParticipant;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.layout_header, this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        groupImageIv = toolbar.findViewById(R.id.iv_group_image);
        groupTitleTv = toolbar.findViewById(R.id.tv_group_name);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
    }

    public void setChannel(BaseChannel channel, String senderId) {
        this.channel = channel;
        boolean isOneToOneConversation = false;
        if (channel instanceof GroupChannel) {
            GroupChannel groupChannel = (GroupChannel) channel;
            if (groupChannel.getParticipants().size() <= 2 && groupChannel.isDistinct()) {
                isOneToOneConversation = true;
            }
        }

        if (isOneToOneConversation) {
            List<Participant> participants = ((GroupChannel) channel).getParticipants();
            for (Participant participant : participants) {
                if (!participant.getId().equals(senderId)) {
                    otherParticipant = participant;
                }
            }
            populateToobar(otherParticipant.getAvatarUrl(), otherParticipant.getDisplayName());
        } else {
            populateToobar(channel.getAvatarUrl(), channel.getName());
        }
        OnTitleClickListener titleClickListener = new OnTitleClickListener(channel, isOneToOneConversation);
        groupTitleTv.setOnClickListener(titleClickListener);
        groupImageIv.setOnClickListener(titleClickListener);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private void populateToobar(String imageUrl, String title) {
        Picasso.with(getContext()).load(imageUrl)
                .placeholder(R.drawable.icon_default_contact)
                .error(R.drawable.icon_default_contact)
                .into(groupImageIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) groupImageIv.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        groupImageIv.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError() {
                        groupImageIv.setImageResource(R.drawable.icon_default_contact);
                    }
                });

        groupTitleTv.setText(title);
    }


    class OnTitleClickListener implements View.OnClickListener {

        private final BaseChannel channel;
        private final boolean isOneToOneConversation;

        public OnTitleClickListener(BaseChannel channel, boolean isOnToOneConversation) {
            this.channel = channel;
            this.isOneToOneConversation = isOnToOneConversation;
        }

        @Override
        public void onClick(View view) {
            if (isOneToOneConversation) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                if (otherParticipant != null) {
                    intent.putExtra(UserProfileActivity.KEY_PARTICIPANT_ID, otherParticipant.getId());
                    intent.putExtra(UserProfileActivity.KEY_GROUP_ID, channel.getId());
                }
                getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.KEY_GROUP_ID, channel.getId());
                getContext().startActivity(intent);
            }
        }
    }
}
