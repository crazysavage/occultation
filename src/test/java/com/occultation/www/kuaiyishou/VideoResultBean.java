package com.occultation.www.kuaiyishou;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Json;
import com.occultation.www.model.JsonBean;

import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-08 11:55
 */
@Content(url=".*.kuaiyinshi.com/api/dou-yin/recommend.*",
        pipeline = "video")
public class VideoResultBean extends JsonBean {

    @Json("$.data")
    private List<VideoBean> data;

    public List<VideoBean> getData() {
        return data;
    }

    public void setData(List<VideoBean> data) {
        this.data = data;
    }
}
