package com.occultation.www.render.json;

import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.AbstractRender;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 17:55
 */
public class JsonRender extends AbstractRender {

    private JsonFieldRender jsonFieldRender;

    public JsonRender() {
        jsonFieldRender = new JsonFieldRender();
    }

    @Override
    public void fieldRender(SpiderBean bean, SpiderResponse res, SpiderRequest req) {
        jsonFieldRender.fieldRender(bean, res, req);
    }
}
