package com.occultation.www.mafengwo.hotel;

import com.alibaba.fastjson.JSON;
import com.occultation.www.annotation.Pipeline;
import com.occultation.www.mafengwo.SqlHelp;
import com.occultation.www.mafengwo.model.Hotel;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IPipeline;
import com.occultation.www.spider.data.QueueContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-27 14:38
 */
@Pipeline("hotel")
public class HotelPipel implements IPipeline {

    private boolean shouldCreate = true;

    @Override
    public void process(SpiderBean bean, SpiderRequest req, SpiderResponse res) {
        ResultBean o = (ResultBean) bean;
        HotelListBean s = o.getList();
        inserts(s.getList());
        if (shouldCreate) {
            createReq(s.getPageCount(),req);
        }
    }


    private static final String SQL = "insert  into `hotel`(`hotel_name`,`intro`,`score`,`position`,`price_info`,`url`,`imgUrl`) "
            + "values (#{hotelName},#{intro},#{score},#{position},#{priceInfo},#{url},#{imgUrl});";


    private void adjustName (HotelPrice price) {
        Document doc = Jsoup.parse(price.getName());
        Elements e = doc.select("img");
        if (e.isEmpty()) {
            price.setName(doc.text());
        } else {
            price.setName(PicNameMapping.getName(e.get(0).attr("src")));
        }

    }

    private void inserts(List<HotelInfoBean> list) {

        for (HotelInfoBean info : list) {
            Hotel hotel = new Hotel();
            hotel.setUrl(info.getUrl());
            hotel.setHotelName(info.getHotelName());
            hotel.setImgUrl(info.getImgUrl());
            hotel.setIntro(info.getIntro());
            hotel.setPosition(info.getPosition());
            hotel.setScore(info.getScore());
            for (HotelPrice price : info.getPrices()) {
                adjustName(price);
            }
            hotel.setPriceInfo(JSON.toJSONString(info.getPrices()));
            SqlHelp.getInstance().insert(SQL, hotel);
        }

    }

    private synchronized void createReq(int pageCount,SpiderRequest req) {
        if (!shouldCreate) {
            return;
        }

        int i = Integer.valueOf(req.getParam("iPage")) + 1;
        for (;i <= pageCount;i++) {
            SpiderRequest subReq = req.subRequest(req.getUrl());
            subReq.addParam("iPage", i+"");
            QueueContext.offer(subReq);
        }
    }
}
