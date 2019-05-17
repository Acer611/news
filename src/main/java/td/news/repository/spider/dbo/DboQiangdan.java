package td.news.repository.spider.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class DboQiangdan {

    private String originAdvice; // （原始数据）比赛提点
    private String originLink; // （原始数据）关联文章链接
    private String home;
    private String visitor;
    private String rate; // 百分比
    private String advice; //  比赛提点/建议
    private List<String> ref = new ArrayList<>(); // 关联的资讯连接
    private int type; // 1.强胆 2.博胆
    private AdviceKeyword adviceResult; // 1.主胜，2.客胜，3.平
    public static final List<String> keywords = Arrays.asList("主胜", "主负", "让负", "让胜", "客队不败", "主队不败");
    private String league;
    private String homeLogo;
    private String visitorLogo;
    private Date date;
    private String matchNo; // 赛事编号
    private String week;
    private boolean rq;
    private String formatDate; // 已格式化的比赛时间
    private String issueNo; // 场次

    public DboQiangdan() {
    }

    public DboQiangdan(String originAdvice, String home, String visitor, String originLink, int type) {
        this.originAdvice = originAdvice;
        this.home = home;
        this.visitor = visitor;
        this.originLink = originLink;
        this.type = type;
    }
}
