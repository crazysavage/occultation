package com.occultation.www.mafengwo.hotel;

import com.occultation.www.annotation.Html;
import com.occultation.www.enums.HtmlFieldTypeEnum;
import com.occultation.www.model.HtmlBean;

import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 14:34
 */
public class HotelInfoBean extends HtmlBean {

    @Html(select = ".hotel-title > div > h3 > a",type= HtmlFieldTypeEnum.ATTR, attr = "href" )
    private String url;

    @Html(select = ".hotel-pic > a > img", type= HtmlFieldTypeEnum.ATTR, attr = "src")
    private String imgUrl;

    @Html(select = ".hotel-title > div > h3 > a")
    private String hotelName;

    @Html(select = ".hotel-info > p")
    private String intro;

    @Html(select = ".hotel-info > ul > li.rating.rating2 > em")
    private String score;

    @Html(select = ".hotel-info > div > span > a")
    private String position;

    @Html(select = ".hotel-btns > a")
    private List<HotelPrice> prices;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<HotelPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<HotelPrice> prices) {
        this.prices = prices;
    }
}
