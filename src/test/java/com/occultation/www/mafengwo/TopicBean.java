package com.occultation.www.mafengwo;

import com.occultation.www.annotation.Ajax;
import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Html;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 17:44
 */
@Content(url = ".*mafengwo.cn/gonglve/ziyouxing/\\d+.html",pipeline = "topic")
public class TopicBean extends HtmlBean {


    @Html(select = "body > div.wrap.clearfix > div.local-con.clearfix > div.sideL > div.l-topic > h1")
    private String title;

    @Html(select = "body > div.wrap.clearfix > div.local-con.clearfix > div.sideL > div.l-topic > div.sub-tit > span:nth-child(1) > em")
    private String readStr;

    @Html(select = "body > div.wrap.clearfix > div.local-con.clearfix > div.sideL > div.l-topic > p")
    private String remark;

    @Html(select = "body > div.wrap.clearfix > div.local-con.clearfix > div.sideL > div._j_content")
    private String content;

    @Ajax(src = "http://www.mafengwo.cn/gonglve/ziyouxing/detail/relation_guides?gid=78726&mddid=10156")
    private AjaxHtmlBean recommend;

    public AjaxHtmlBean getRecommend() {
        return recommend;
    }

    public void setRecommend(AjaxHtmlBean recommend) {
        this.recommend = recommend;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReadStr() {
        return readStr;
    }

    public void setReadStr(String readStr) {
        this.readStr = readStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
