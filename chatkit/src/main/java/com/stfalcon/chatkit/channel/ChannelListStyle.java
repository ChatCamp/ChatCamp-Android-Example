package com.stfalcon.chatkit.channel;

import android.content.Context;
import android.util.AttributeSet;

import com.stfalcon.chatkit.commons.Style;

/**
 * Created by shubhamdhabhai on 18/05/18.
 */

public class ChannelListStyle extends Style {


    protected ChannelListStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ChannelListStyle parseStyle(Context context, AttributeSet attrs){
        ChannelListStyle style = new ChannelListStyle(context, attrs);
        // add custom items here
        return style;
    }
}
