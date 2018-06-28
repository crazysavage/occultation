package com.occultation.www.mafengwo;

import com.occultation.www.EngineBuilder;
import com.occultation.www.net.SpiderRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 17:49
 */
public class SpiderTask {

    public static void main(String[] args) {
        SpiderRequest request = new SpiderRequest("http://www.mafengwo.cn/hotel/ajax.php?iMddId=10156&iAreaId=-1&iRegionId=-1&iPoiId=&position_name=&nLat=0&nLng=0&iDistance=10000&sCheckIn=2018-08-05&sCheckOut=2018-08-06&iAdultsNum=2&iChildrenNum=0&sChildrenAge=&iPriceMin=&iPriceMax=&sTags=&sSortType=comment&sSortFlag=DESC&has_booking_rooms=0&has_faved=0&sKeyWord=&sAction=getPoiList5");
        request.addParam("iPage","1");

        EngineBuilder.create()
                .setClasspath("com.occultation.www.mafengwo")
                .setDeep(3)
                .setInterval(2000)
                .setSpiderSize(3)
                .setSeed(request)
                .setCanExtractHref(false)
                .build().start();

    }

}
