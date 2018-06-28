package com.occultation.www.mafengwo.hotel;

import com.occultation.www.annotation.Html;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 14:55
 */
public class HotelPrice extends HtmlBean {

    @Html(select = ".name")
    private String name;

    @Html(select = ".price > strong:nth-child(2)")
    private Integer lastMoney;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLastMoney() {
        return lastMoney;
    }

    public void setLastMoney(Integer lastMoney) {
        this.lastMoney = lastMoney;
    }
}
