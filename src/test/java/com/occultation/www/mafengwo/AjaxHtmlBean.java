package com.occultation.www.mafengwo;

import com.occultation.www.annotation.Json;
import com.occultation.www.model.JsonBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 19:42
 */
public class AjaxHtmlBean extends JsonBean {

    @Json("$.html")
    private String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
