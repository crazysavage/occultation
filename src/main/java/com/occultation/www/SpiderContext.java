package com.occultation.www;

import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.IFetch;
import com.occultation.www.render.IPipeline;
import com.occultation.www.render.IRender;

import java.util.Objects;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 13:18
 */
public class SpiderContext {
    /**
     * 爬虫bean
     */
    private Class<? extends SpiderBean> beanClazz;
    /**
     * 爬取器
     */
    private IFetch fetch;
    /**
     * 渲染器
     */
    private IRender render;

    /**
     * 渲染完成后的后续管道
     */
    private IPipeline pipeline;

    public SpiderContext(Class<? extends SpiderBean> beanClazz,IFetch fetch,IRender render,IPipeline pipeline) {
        this.beanClazz = beanClazz;
        this.fetch = fetch;
        this.render = render;
        this.pipeline = pipeline;
    }

    public IFetch getFetch() {
        return fetch;
    }

    public void setFetch(IFetch fetch) {
        this.fetch = fetch;
    }

    public IRender getRender() {
        return render;
    }

    public Class<? extends SpiderBean> getBeanClazz() {
        return beanClazz;
    }

    public IPipeline getPipeline() {
        return pipeline;
    }

    public String getBeanName() {
        return this.beanClazz.getName();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SpiderContext && ((SpiderContext) other).getBeanName().equals(this.getBeanName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getBeanName());
    }


}
