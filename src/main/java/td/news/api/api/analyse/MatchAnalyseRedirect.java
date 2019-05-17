package td.news.api.api.analyse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author sanlion do
 */
@Controller
@RequestMapping("/football/analyse")
public class MatchAnalyseRedirect {

    private static final String error = "http://h5.cp.lovedou.com/static/noData.html";


    @GetMapping("/{mid}")
    public void detail(
            @PathVariable String mid, HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        // 走网易的赛事分析页面
        try {
            net(mid, request, response);
        } catch (Exception e) {
            response.sendRedirect(error);
        }
    }

    public void net(String mid, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Document document = Jsoup.connect("http://zx.caipiao.163.com/football/wap_matchDetail.html?matchId=" + mid)
                .header("Accept", request.getHeader("Accept"))
                .header("User-Agent", request.getHeader("User-Agent"))
                .header("Host", "zx.caipiao.163.com")
                .header("Referer", "http://caipiao.163.com/t/jczq/?isClearCookie=1&topHi=false")
                .get();

        Element jyOdds = document.select(".jyOdds").first();
        jyOdds.select("h3").get(1).remove();
        jyOdds.select("table").get(1).remove();
        Element iscrolljs = document.head().child(18);
        iscrolljs.attr("src", "http://common-1253410441.costj.myqcloud.com/css/iscroll.js");
        document.head().insertChildren(18, Collections.singletonList(iscrolljs));
        document.head().appendChild(new Element("style").html(".tabAnaly .analyTitle >span{width: 24%;}"));
        String html = document.html();
        html = html.replaceAll("网易指数", "指数");
        html = html.replaceAll("&gt;", ">");
        response.setCharacterEncoding("utf-8");
        response.getWriter().println(html);
    }

}
