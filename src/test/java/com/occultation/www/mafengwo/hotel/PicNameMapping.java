package com.occultation.www.mafengwo.hotel;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 16:33
 */
public class PicNameMapping {

    private static final Map<String,String> map;

    static {
        map = Maps.newHashMap();
        map.put("http://images.mafengwo.net/images/hotel/newlogo/list-new-logo-zx@2x.png","马蜂窝");
        map.put("http://images.mafengwo.net/images/hotel/ota/booking_w100h20_2x_2.png","Booking.com");
        map.put("http://images.mafengwo.net/images/hotel/ota/ctrip_w100h20_2x_2.png","携程");
        map.put("http://images.mafengwo.net/images/hotel/ota/agoda_w100h20_2x_2.png","agoda");
        map.put("http://images.mafengwo.net/images/hotel/ota/airbnb_w100h20_2x_3.png","爱彼迎");
    }


    public static String getName(String url) {
        String name = map.get(url);
        return name == null ? url : name;
    }
}
