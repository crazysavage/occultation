package com.occultation.www.render;

import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 16:49
 */
public interface IFieldRender {
    void fieldRender(SpiderBean bean,SpiderResponse res,SpiderRequest req);

}
