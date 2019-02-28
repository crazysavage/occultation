package com.occultation.www.dazong;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Html;
import com.occultation.www.model.HtmlBean;

import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-09-17 13:43
 */
@Content(url = ".*.dianping.com/hangzhou/ch10/.*" , pipeline = "shop")
public class ShopListBean extends HtmlBean {

    @Html(select = "#shop-all-list > ul > li")
    private List<ShopBean> shops;

    public List<ShopBean> getShops() {
        return shops;
    }

    public void setShops(List<ShopBean> shops) {
        this.shops = shops;
    }
}
