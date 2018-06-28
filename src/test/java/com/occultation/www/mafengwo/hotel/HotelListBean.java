package com.occultation.www.mafengwo.hotel;

import com.occultation.www.annotation.Html;
import com.occultation.www.model.HtmlBean;

import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 15:13
 */
public class HotelListBean extends HtmlBean{

    @Html(select = ".hotel-item")
    private List<HotelInfoBean> list;

    @Html(select = "#list_paginator > span.count > span:nth-child(1)")
    private Integer pageCount;

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<HotelInfoBean> getList() {
        return list;
    }

    public void setList(List<HotelInfoBean> list) {
        this.list = list;
    }
}
