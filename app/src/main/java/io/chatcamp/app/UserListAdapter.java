package io.chatcamp.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.utils.CircleTransform;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.sdk.User;

/**
 * Created by shubhamdhabhai on 22/05/18.
 */

public class UserListAdapter  extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder>{

    private final Context context;

    private List<User> userList;

    private UserClickListener userClickListener;

    public interface UserClickListener {
        void onUserClicked(User user);
    }

    public UserListAdapter(Context context) {
        this.context = context;
        userList = new ArrayList<>();
    }

    public void setUserClickListener(UserClickListener userClickListener) {
        this.userClickListener = userClickListener;
    }

    public void clear() {
        userList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<User> users) {
        userList.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserListViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView nameTv;
        private ImageView onlineIndicator;

        public UserListViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
            nameTv = itemView.findViewById(R.id.tv_name);
            onlineIndicator = itemView.findViewById(R.id.iv_online);
        }

        public void bind(final User user) {
            Picasso.with(context).load(user.getAvatarUrl())
                    .placeholder(com.stfalcon.chatkit.R.drawable.icon_default_contact)
                    .transform(new CircleTransform()).into(avatar);
            nameTv.setText(user.getDisplayName());
            if(user.isOnline()) {
                onlineIndicator.setVisibility(View.VISIBLE);
            } else {
                onlineIndicator.setVisibility(View.GONE);
            }
            itemView.setTag(user);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getTag() != null && v.getTag() instanceof User) {
                        if(userClickListener != null) {
                            userClickListener.onUserClicked((User) v.getTag());
                        }
                    }
                }
            });
        }
    }
}
