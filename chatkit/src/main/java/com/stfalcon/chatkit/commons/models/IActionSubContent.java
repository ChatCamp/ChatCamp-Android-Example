package com.stfalcon.chatkit.commons.models;

import java.util.List;

/**
 * Created by shubhamdhabhai on 22/03/18.
 */

public interface IActionSubContent {
    public String getId();
    public String getHeading();
    public List<String> getContents();
    public List<String> getActions();
    public String getImageUrl();
}
