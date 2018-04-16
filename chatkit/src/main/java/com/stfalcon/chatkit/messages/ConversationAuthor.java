package com.stfalcon.chatkit.messages;

import com.stfalcon.chatkit.commons.models.IUser;

import io.chatcamp.sdk.User;

/**
 * Created by ChatCamp Team on 05/02/18.
 */

public class ConversationAuthor implements IUser {

    private User user;

    public ConversationAuthor(User user) {
        this.user = user;
    }

    @Override
    public String getId() {
        return user.getId();
    }

    @Override
    public String getName() {
        return user.getDisplayName();
    }

    @Override
    public String getAvatar() {
        return user.getAvatarUrl();
    }

}
