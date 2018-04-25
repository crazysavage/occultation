package com.occultation.www;

import com.occultation.www.model.HtmlBean;

import java.util.List;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 13:29
 */
public class TestBean extends HtmlBean {

    public Long olv;
    public int iv;
    public boolean bv;
    public List<Long> list;
    private String str;

    public TestBean createNext() {
        TestBean bean = new TestBean();
        bean.setStr(str);
        return bean;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Long getOlv() {
        return olv;
    }

    public void setOlv(Long olv) {
        this.olv = olv;
    }

    public int getIv() {
        return iv;
    }

    public void setIv(int iv) {
        this.iv = iv;
    }

    public boolean isBv() {
        return bv;
    }

    public void setBv(boolean bv) {
        this.bv = bv;
    }

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }
}
