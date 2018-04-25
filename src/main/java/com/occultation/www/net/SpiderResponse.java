package com.occultation.www.net;

/**
 * @Type SpiderResponse
 * @Desc TODO
 * @author liss
 * @created 2017年7月1日 下午6:15:04
 * @version 1.0.0
 */
public class SpiderResponse {
    private int status;
    private String charset;
    private String content;
    private String contentType;

    public SpiderResponse getSubResponse(String content) {
        SpiderResponse spiderResponse = new SpiderResponse();
        spiderResponse.setCharset(charset);
        spiderResponse.setContentType(contentType);
        spiderResponse.setStatus(status);
        spiderResponse.setContent(content);
        return spiderResponse;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    
}
