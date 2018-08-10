package com.occultation.www.kuaiyishou;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Json;
import com.occultation.www.model.JsonBean;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-07 18:19
 */


public class VideoBean extends JsonBean {

    @Json("$.desc")
    private String desc;

    @Json("$.nickname")
    private String authorName;

    @Json("$.video_url")
    private String videoUrl;

    @Json("$.statistics.zan")
    private Integer support;

    @Json("$.statistics.share")
    private Integer share;

    @Json("$.statistics.play")
    private Integer play;

    @Json("$.statistics.comment")
    private Integer comment;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getSupport() {
        return support;
    }

    public void setSupport(Integer support) {
        this.support = support;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getPlay() {
        return play;
    }

    public void setPlay(Integer play) {
        this.play = play;
    }

    public Integer getComment() {
        return comment;
    }

    public void setComment(Integer comment) {
        this.comment = comment;
    }
}
