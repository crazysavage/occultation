package com.occultation.www.kuaiyishou;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-09-07 10:42
 */
public class SinglePublish {

    public static void main(String[] args) {
        Video v = new Video();
        v.setDesc("不可描述的小视频");
        v.setHeight(640);
        v.setSignature("dhsaddsfdsafaldjksaddsa");
        v.setVideo_url("/testporn.mp4");
        v.setWidth(320);
            new PublishContent(v).run();
    }


}
