package com.stfalcon.chatkit.utils;

/**
 * Created by shubhamdhabhai on 17/05/18.
 */

public interface HeaderViewClickListener {
    void onHeaderViewClicked(String channelId, boolean isOneToOneConversation, String otherParticipantId);
}
