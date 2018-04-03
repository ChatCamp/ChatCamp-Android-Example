package com.stfalcon.chatkit.commons.models;

import java.util.List;

/**
 * Created by shubhamdhabhai on 22/03/18.
 */

public interface IActionContent {

    public String getId();
    public String getImageUrl();
    public String getTitle() ;
    public List<IActionSubContent> getContents();
    public List<String> getActions() ;
    public void setContents(List<IActionSubContent> actionSubContents);
    public void setActions(List<String> actions) ;

}
