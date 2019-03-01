package com.occultation.www.kuaiyishou;

import com.occultation.www.annotation.Pipeline;
import com.occultation.www.mafengwo.SqlHelp;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IPipeline;
import com.occultation.www.spider.SpiderThreadLocal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-07 18:27
 */
@Pipeline("video")
public class VideoPipeline implements IPipeline {

    private static final Logger log = LoggerFactory.getLogger(VideoPipeline.class);

    private static AtomicInteger count = new AtomicInteger(0);
    private static final int max = 6000;

    private static final String SQL = "INSERT INTO `test`.`short_video` (`desc`,`author_name`,`video_url`,`support`,`share`,`play`,`comment`) VALUES"
            + "(#{desc},#{authorName},#{videoUrl},#{support},#{share},#{play},#{comment})";

    @Override
    public void process(SpiderBean bean, SpiderRequest req, SpiderResponse res) {
        VideoResultBean resultBean = (VideoResultBean)bean;
        resultBean.getData().forEach(video -> {
            if (StringUtils.isEmpty(video.getDesc())) {
                return;
            }

            String url = video.getVideoUrl();
            //下载视频
            String filePath = FileDownLoad.download(UrlEncrypt.encrypt(url));
            if (filePath != null) {
                video.setVideoUrl(filePath);
            }
            //数据入库
            SqlHelp.getInstance().insert(SQL,video);
            log.info("download video, name={},desc={}",filePath,video.getDesc());

          if (count.incrementAndGet() > max) {
            SpiderThreadLocal.get().stop();
          }
        });



    }




}
