package com.occultation.www;

import com.occultation.www.annotation.Pipeline;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.render.IPipeline;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 23:11
 */
@Pipeline("blogPipeline")
public class BlogPipeline implements IPipeline {
    @Override
    public void process(SpiderBean bean) {
        BlogBean blogBean = (BlogBean) bean;
        System.out.println("------------->"+blogBean.getTitle());
        System.out.println("------------->"+blogBean.getAuthor());
    }
}
