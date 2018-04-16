package com.stfalcon.chatkit.messages.customContent;

import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IActionSubContent;

import java.util.List;

/**
 * Created by shubhamdhabhai on 22/03/18.
 */

public class ActionSubContent implements IActionSubContent{
    private String id;
    private String heading;
    @SerializedName("image_url")
    private String imageUrl;
    private List<String> contents;
    private List<String> actions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
