package com.occultation.www.render;

import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 18:57
 */
public interface IPipeline {
    void process(SpiderBean bean, SpiderRequest req, SpiderResponse res);
}
