package com.occultation.www.gif.yge;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Html;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-11 23:43
 */
@Content(url=".*\\.yge.cc/\\d+.html(/\\d+)?",
        pipeline = "yge")
public class ArticleBean extends HtmlBean {
    @Html(select = ".content")
    private String article;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
