package com.occultation.www.zhihu;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Html;
import com.occultation.www.annotation.UrlParam;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-12 19:55
 */
@Content(url = ".*zhihu.com/p/{pid}",pipeline = "articlePipeline")
public class ArticleBean extends HtmlBean {

    @UrlParam
    private Long pid;

    @Html(select = "#root > div > main > div > article > header > h1")
    private String title;

    @Html(select = "#root > div > main > div > article > div:nth-child(2) > div")
    private String content;

    @Html(select = "#Popover-83824-16394-toggle > a")
    private String authName;

    @Html(select = "#root > div > main > div > article > header > div:nth-child(3) > span > button")
    private String supports;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getSupports() {
        return supports;
    }

    public void setSupports(String supports) {
        this.supports = supports;
    }
}
