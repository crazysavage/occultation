package com.occultation.www.dazong;

import com.occultation.www.annotation.Ajax;
import com.occultation.www.annotation.Html;
import com.occultation.www.enums.HtmlFieldTypeEnum;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-09-17 13:42
 */
public class ShopBean extends HtmlBean {

    @Html(select = ".tit" , type = HtmlFieldTypeEnum.TEXT)
    private String shopName;

    @Html(select = ".pic img", type = HtmlFieldTypeEnum.ATTR, attr = "data-src")
    private String photo;

    @Html(select = ".review-num", type = HtmlFieldTypeEnum.TEXT)
    private String comments;

    @Html(select = ".mean-price", type = HtmlFieldTypeEnum.TEXT)
    private String cost;

    @Html(select = ".sml-rank-stars", type = HtmlFieldTypeEnum.ATTR, attr = "class")
    private String star;

    @Html(select = ".comment-list", type = HtmlFieldTypeEnum.TEXT)
    private String score;

    @Html(select = ".tag")
    private String type;

    @Html(select = ".addr")
    private String location;

    @Html(select = ".recommend", type = HtmlFieldTypeEnum.TEXT)
    private String desc;

    @Ajax(src = "https://restapi.amap.com/v3/geocode/geo?address={location}&key=a808531128cccca4046cccb318703adb&city=杭州")
    private PoiBean poi;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public PoiBean getPoi() {
        return poi;
    }

    public void setPoi(PoiBean poi) {
        this.poi = poi;
    }
}
