package com.stfalcon.chatkit.messages.customContent;

import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IActionContent;
import com.stfalcon.chatkit.commons.models.IActionSubContent;

import java.util.List;

/**
 * Created by shubhamdhabhai on 22/03/18.
 */

public class ActionContent implements IActionContent{

    private String id;
    @SerializedName("image_url")
    private String imageUrl;
    private String title;
    private List<IActionSubContent> contents;
    private List<String> actions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<IActionSubContent> getContents() {
        return contents;
    }

    public void setContents(List<IActionSubContent> contents) {
        this.contents = contents;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}
