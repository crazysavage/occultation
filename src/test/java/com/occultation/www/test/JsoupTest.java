package com.occultation.www.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-22 16:33
 */
public class JsoupTest {


    public static void main(String[] args) {
        String message = "<div>             <p>听骨链重建术技术起自２０世纪初即被用来提高听力，此后出现了很多各种不同的听骨链重建的方法。直到２０世纪５０年代，两位欧美医生的工作奠定了现代听骨重建技术的基础。与之相伴的外科技术以及材料科学的进步，使得中耳重建手术的效果逐步提高。可明显改善患者听力水平。人工听骨链重建术主要用于慢性化脓性中<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳炎</a>患者的<a href=\"http://www.haodf.com/jibing/erlong.htm\">听力重建</a>。</p><p><br></p><p>慢性化脓性中<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳炎</a>是中耳黏膜、骨膜或深达骨质的慢性化脓性炎症。本病是耳鼻咽喉头颈外科最常见的炎症之一，临床上以耳内反复流脓、鼓膜穿孔及听力减退为特点，如果伴有胆脂瘤的发生还可能导致严重的颅内外并发症。</p><p><br></p><p>慢性化脓性中<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳炎</a>分为三型：1.单纯型：最常见，病情较轻，<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳流脓</a>多为间歇性，一般不臭。常伴轻度耳鸣或听力减退，经治疗可暂时痊愈，但常易复发。2.骨疡型：又称坏死型或肉芽型，组织破坏较广泛，病变深达骨质，听小骨、鼓窦周围组织可发生坏死，局部有肉芽组织或息肉形成。<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳流脓</a>多为持续性，脓性间有血丝，鼓膜穿孔多数在边缘，并且传导性聋较重。3.胆脂瘤型：亦称危险型，鼓室或鼓窦内形成上皮团块，因能压迫破坏骨质，具有恶性肿瘤性质，故过去错误地称为胆脂瘤，实质上并非肿瘤，<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳流脓</a>不多但味奇臭，穿孔内可见有白色碎块，豆腐渣样胆脂瘤上皮团，因骨质广泛破坏，可并发面瘫、颅内感染等严重并发症，可危及生命，因此一旦确诊胆脂瘤，应尽早做手术治疗。无论是何型慢性化脓性中<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳炎</a>都能改变中耳的正常解剖结构，影响声音的传导，是致聋的主要病因。</p><p><br></p><p>近年来，慢性中<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳炎</a>相关手术技术和应用材料迅速发展。除了可以使用自身材料外，各种人工材料，如陶瓷、钛合金材料相继问世，这些材料在生物相容性和声传导方面效果满意。符合重建听骨所需条件的赝复体应具备：生物相容性好，容易植入且稳定，传声能力强的特点。</p><p><br></p><p>慢性中<a href=\"http://www.haodf.com/jibing/erjibing.htm\">耳炎</a>的手术目的逐渐注重于恢复中耳腔的正常生理结构，提高术后听力水平。部分患者根据术中不同情况采用不同的人工听骨材料修复听骨链，可一期重建听力。对于病变非常广泛的病例，即便不能一期<a href=\"http://www.haodf.com/jibing/erlong.htm\">听力重建</a>，也会在术中创造机会为二期<a href=\"http://www.haodf.com/jibing/erlong.htm\">听力重建</a>做准备。</p></div>";
        Document document = Jsoup.parse(message);
        Elements els = document.select("a");
        System.out.println("<------------------------原文------------------->");
        System.out.println(document.body().html());
        System.out.println("");
        System.out.println("<------------------------a标签内容------------------->");
        for (Element a : els) {
            System.out.println(a.html());
            a.after(a.html());
            a.remove();
        }
        System.out.println("<------------------------过滤完a标签后------------------->");
        System.out.println(document.body().html());
    }
}
