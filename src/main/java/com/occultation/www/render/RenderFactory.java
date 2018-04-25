package com.occultation.www.render;

import com.occultation.www.enums.RenderTypeEnum;
import com.occultation.www.render.html.HtmlRender;
import com.occultation.www.render.json.JsonRender;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 17:54
 */
public class RenderFactory {

    public IRender create(RenderTypeEnum type) {
        switch (type) {
            case html:
                return new HtmlRender();
            case json:
                return new JsonRender();
            default:
                throw new IllegalArgumentException("type is undefined");
        }

    }

}
