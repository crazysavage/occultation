package com.occultation.www.render.html;

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
public class HtmlRender extends AbstractRender {
    private HtmlFiledRender htmlFiledRender;

    public HtmlRender() {
        this.htmlFiledRender = new HtmlFiledRender();
    }

    @Override
    public void fieldRender(SpiderBean bean, SpiderResponse res, SpiderRequest req) {
        htmlFiledRender.fieldRender(bean, res, req);
    }


}
