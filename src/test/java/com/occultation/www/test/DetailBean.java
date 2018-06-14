package com.occultation.www.test;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Html;
import com.occultation.www.enums.HtmlFieldTypeEnum;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 16:46
 */
@Content(url="http://sns-h5.guahao-inc.com/question/.*")
public class DetailBean extends HtmlBean {

    @Html(select = "#scroller .post-content-block .post-content",type = HtmlFieldTypeEnum.TEXT)
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
