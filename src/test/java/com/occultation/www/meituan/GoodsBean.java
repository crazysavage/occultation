package com.occultation.www.meituan;

import com.occultation.www.annotation.Content;
import com.occultation.www.render.json.JsonRender;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-12 19:55
 */
@Content(url = "http://hz.meituan.com/meishi/pn\\d+/",pipeline = "goodsPipeline")
public class GoodsBean extends JsonRender {

}
