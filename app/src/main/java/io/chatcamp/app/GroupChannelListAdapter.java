package io.chatcamp.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.chatcamp.sdk.ChatCampException;
import io.chatcamp.sdk.GroupChannel;
import io.chatcamp.sdk.Participant;

/**
 * Created by ChatCamp Team on 29/11/17.
 */

public class GroupChannelListAdapter extends RecyclerView.Adapter<GroupChannelListAdapter.ViewHolder> {

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    private List<GroupChannel> mDataset;
    private RecyclerViewClickListener mListener;
    private Context context;

    public GroupChannelListAdapter(Context context, List<GroupChannel> myDataset, RecyclerViewClickListener listener) {
        mDataset = myDataset;
        mListener = listener;
        this.context = context;
    }

    @Override
    public GroupChannelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_channel_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private RecyclerViewClickListener mListener;
        TextView titleTv;
        ImageView avatarIv;
        TextView timeTv;
        TextView lastMessageTv;
        TextView unreadMessageTv;

        public ViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            titleTv = v.findViewById(R.id.tv_title);
            avatarIv = v.findViewById(R.id.iv_avatar);
            timeTv = v.findViewById(R.id.tv_time);
            lastMessageTv = v.findViewById(R.id.tv_last_message);
            unreadMessageTv = v.findViewById(R.id.tv_unread_message);
            mListener = listener;
            v.setOnClickListener(this);
        }

        public void bind(GroupChannel groupChannel) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (groupChannel.getLastMessage() != null) {
                Date date = new Date(groupChannel.getLastMessage().getInsertedAt() * 1000);
                timeTv.setText(format.format(date));
                if (groupChannel.getLastMessage().getType().equalsIgnoreCase("text")) {
                    lastMessageTv.setText(groupChannel.getLastMessage().getText());
                }
            }
            if (groupChannel.getUnreadMessageCount() > 0) {
                unreadMessageTv.setVisibility(View.VISIBLE);
                unreadMessageTv.setText(String.valueOf(groupChannel.getUnreadMessageCount()));
            } else {
                unreadMessageTv.setVisibility(View.GONE);
            }
            if (groupChannel.getParticipantsCount() <= 2 && groupChannel.isDistinct()) {
                GroupChannel.get(groupChannel.getId(), new GroupChannel.GetListener() {
                    @Override
                    public void onResult(GroupChannel groupChannel, ChatCampException e) {
                        List<Participant> participants = groupChannel.getParticipants();
                        for (Participant participant : participants) {
                            if (!participant.getId().equals(LocalStorage.getInstance().getUserId())) {
                                populateTitle(participant.getAvatarUrl(), participant.getDisplayName());
                            }
                        }
                    }
                });

            } else {
                populateTitle(groupChannel.getAvatarUrl(), groupChannel.getName());
            }

        }

        private void populateTitle(String imageUrl, String title) {
            Picasso.with(context).load(imageUrl)
                    .placeholder(R.drawable.icon_default_contact)
                    .error(R.drawable.icon_default_contact)
                    .into(avatarIv, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap imageBitmap = ((BitmapDrawable) avatarIv.getDrawable()).getBitmap();
                            RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
                            imageDrawable.setCircular(true);
                            imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                            avatarIv.setImageDrawable(imageDrawable);
                        }

                        @Override
                        public void onError() {
                            avatarIv.setImageResource(R.mipmap.ic_launcher_round);
                        }
                    });

            titleTv.setText(title);

        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
        }
    }

}






