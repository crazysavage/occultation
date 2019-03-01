package com.occultation.www.dazong;

import com.occultation.www.annotation.Json;
import com.occultation.www.model.JsonBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-09-17 14:18
 */
public class PoiBean extends JsonBean {

    @Json("$.geocodes[0].location")
    private String poi;

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }
}
