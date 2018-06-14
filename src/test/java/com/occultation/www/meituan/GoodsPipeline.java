package com.occultation.www.meituan;

import com.occultation.www.annotation.Pipeline;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.render.IPipeline;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-12 19:56
 */
@Pipeline("goodsPipeline")
public class GoodsPipeline implements IPipeline {

    @Override
    public void process(SpiderBean bean) {
        System.out.println(bean.toString());
    }
}
