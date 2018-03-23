package io.chatcamp.app.customContent;

import com.stfalcon.chatkit.commons.models.IActionContent;
import com.stfalcon.chatkit.commons.models.IActionMessage;

import java.util.List;

/**
 * Created by shubhamdhabhai on 07/02/18.
 */


public class ActionMessage implements IActionMessage {

    @Override
    public List<IActionContent> getActionContents() {
        return actionContents;
    }

    public void setActionContents(List<IActionContent> actionContents) {
        this.actionContents = actionContents;
    }

    private List<IActionContent> actionContents;

//    private int $id;
//    private int ProductID;
//    private String Name;
//    private String Code;
//    private String ImageURL;
//    private String ShortDescription;
//    private String LongDescription;
//    private int CategoryID;
//    private int ShippingCost;
//    private int Status;
//    private int BrandID;
//
//
//    public int get$id() {
//        return $id;
//    }
//
//    public void set$id(int $id) {
//        this.$id = $id;
//    }
//
//    public int getProductID() {
//        return ProductID;
//    }
//
//    public void setProductID(int productID) {
//        ProductID = productID;
//    }
//
//    public String getName() {
//        return Name;
//    }
//
//    public void setName(String name) {
//        Name = name;
//    }
//
//    public String getCode() {
//        return Code;
//    }
//
//    public void setCode(String code) {
//        Code = code;
//    }
//
//    public String getImageURL() {
//        return ImageURL;
//    }
//
//    public void setImageURL(String imageURL) {
//        ImageURL = imageURL;
//    }
//
//    public String getShortDescription() {
//        return ShortDescription;
//    }
//
//    public void setShortDescription(String shortDescription) {
//        ShortDescription = shortDescription;
//    }
//
//    public String getLongDescription() {
//        return LongDescription;
//    }
//
//    public void setLongDescription(String longDescription) {
//        LongDescription = longDescription;
//    }
//
//    public int getCategoryID() {
//        return CategoryID;
//    }
//
//    public void setCategoryID(int categoryID) {
//        CategoryID = categoryID;
//    }
//
//    public int getShippingCost() {
//        return ShippingCost;
//    }
//
//    public void setShippingCost(int shippingCost) {
//        ShippingCost = shippingCost;
//    }
//
//    public int getStatus() {
//        return Status;
//    }
//
//    public void setStatus(int status) {
//        Status = status;
//    }
//
//    public int getBrandID() {
//        return BrandID;
//    }
//
//    public void setBrandID(int brandID) {
//        BrandID = brandID;
//    }
}
