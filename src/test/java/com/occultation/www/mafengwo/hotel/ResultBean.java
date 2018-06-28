package com.occultation.www.mafengwo.hotel;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Json;
import com.occultation.www.model.JsonBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 15:16
 */
@Content(url=".*.mafengwo.cn/hotel/ajax.php?.*",
        pipeline = "hotel")
public class ResultBean extends JsonBean {

    @Json("$.html")
    private HotelListBean list;

    public HotelListBean getList() {
        return list;
    }

    public void setList(HotelListBean list) {
        this.list = list;
    }
}
