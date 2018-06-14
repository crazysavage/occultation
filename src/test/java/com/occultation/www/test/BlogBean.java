package com.occultation.www.test;

import com.occultation.www.annotation.Ajax;
import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Html;
import com.occultation.www.annotation.UrlParam;
import com.occultation.www.enums.HtmlFieldTypeEnum;
import com.occultation.www.model.HtmlBean;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 22:50
 */

@Content(url = "(https|http)://www.cnblogs.com/{authorId}/p/{postId}.html",pipeline = "blogPipeline")
public class BlogBean extends HtmlBean {

    @UrlParam
    private Long postId;

    @UrlParam
    private Long authorId;

    @Html(select = ".postTitle a",type = HtmlFieldTypeEnum.TEXT)
    private String title;
    @Html(select = "#Header1_HeaderTitle",type = HtmlFieldTypeEnum.TEXT)
    private String author;

    @Html(select = "#BlogPostCategory",type = HtmlFieldTypeEnum.TEXT)
    private String postCategory;

    @Ajax(src="http://www.cnblogs.com/mvc/blog/ViewCountCommentCout.aspx?postId={postId}")
    private Integer viewCount;

    @Html(select = "#digg_count")
    private Integer diggCount;

    @Ajax(src = "http://www.cnblogs.com/mvc/blog/GetComments.aspx?postId={postId}&blogApp={authorId}&pageIndex=0")
    private Integer subPostCount;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getDiggCount() {
        return diggCount;
    }

    public void setDiggCount(Integer diggCount) {
        this.diggCount = diggCount;
    }

    public Integer getSubPostCount() {
        return subPostCount;
    }

    public void setSubPostCount(Integer subPostCount) {
        this.subPostCount = subPostCount;
    }
}
