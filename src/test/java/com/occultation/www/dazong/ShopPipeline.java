package com.occultation.www.dazong;

import com.occultation.www.annotation.Pipeline;
import com.occultation.www.mafengwo.SqlHelp;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IPipeline;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-09-17 14:22
 */
@Pipeline("shop")
public class ShopPipeline implements IPipeline {

    private static Logger log = LoggerFactory.getLogger(ShopPipeline.class);

    String sql = "INSERT INTO `test`.`shop` (  `title`,  `desc`,  `tag`,  `lat`,  `lng`,  `location`,  `file`) "
        + "VALUES  (    #{title},    #{desc},    #{tag},    #{lat},    #{lng},    #{location},    #{file}  ) ";

    @Override
    public void process(SpiderBean bean, SpiderRequest req, SpiderResponse res) {
        log.info("url :{} \n{}\n",req.getUrl(),bean.toString());
        ShopListBean resBean = (ShopListBean) bean;
        List<ShopBean> shops = resBean.getShops();
        shops.forEach(shop -> {
            ShopInfo info = new ShopInfo();
            info.setTitle(shop.getShopName());
            info.setTag(shop.getType());
            info.setDesc(createDesc(shop));
            info.setLocation(shop.getLocation());
            String[] poi = shop.getPoi().getPoi().split(",");
            info.setLat(poi[1]);
            info.setLng(poi[0]);
            FileInfo fileInfo = HttpExecute.download(shop.getPhoto(),System.currentTimeMillis()+"");
            if (fileInfo != null) {
                log.info(fileInfo.toString());
                info.setFile(fileInfo.toString());
            } else {
                info.setFile(shop.getPhoto());
            }
            SqlHelp.getInstance().insert(sql,info);
        });

    }


    private String createDesc(ShopBean shop) {
        StringBuilder sb = new StringBuilder();
        sb.append(shop.getScore());
        sb.append(" 星级：");
        sb.append(Double.parseDouble(shop.getStar().replaceAll("sml-rank-stars sml-str","")) / 10.0);
        sb.append("\n");
        sb.append(shop.getCost());
        sb.append("\n");
        sb.append(shop.getDesc());
        return sb.toString();

    }
}
