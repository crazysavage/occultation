package com.occultation.www.render.type;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-30 10:56
 */
public class DateTypeHandler extends AbstractTypeHandler<Date> {
    @Override
    protected Date convert(Object value) throws Exception {
        return getValue(value,"yyyy-MM-dd HH:mm:ss");
    }

    public Date getValue(Object src, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(src.toString());
    }
}
