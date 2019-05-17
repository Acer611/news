package td.news.biz.news.domain;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import td.news.common.Moneys;
import td.news.common.Times;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.message.dbo.DboBetOpen;

import java.text.MessageFormat;
import java.util.Date;

@Data
public class News {

    private long id;
    private String title;
    private String icon;
    private String link;
    private Date create;
    private Date update;
    private int timeState = 1;
    private Date begin;
    private Date end;
    private String source;
    private String author;
    private int sort;
    private String origin;
    String content;
    String bootContent;
    int bootType;
    boolean lotteryWinNotice;
    int readCounts;
    String typeCode;
    int contentType;//资讯内容类型 0-富文本 1-视频
    String code;
    public News() {
    }

    public News(DbNews dboNews) {
        this.id = dboNews.getID();
        this.title = dboNews.getTitle();
        this.update = dboNews.getUpdateTime();
        this.icon = dboNews.getIconUrl();
        this.link = dboNews.getLinkUrl();
        this.begin = dboNews.getStartTime();
        this.end = dboNews.getEndTime();
        this.timeState = Times.state(this.begin, this.end);
        this.create = dboNews.getCreateTime();
        this.source = dboNews.getOrigin();
        this.author = dboNews.getAuthor();
        this.sort = dboNews.getOrderNo();
        this.origin = dboNews.getOrigin();
        this.content = dboNews.getContent();
        this.bootContent = dboNews.getBootContent();
        this.bootType = dboNews.getPushTypeID();
        this.readCounts = dboNews.getReadCount();
       /* this.typeCode = dboNews.getCode();*/
        this.contentType=dboNews.getContentType();
        this.typeCode = String.valueOf(dboNews.getTypeID());
        this.code = dboNews.getCode();
    }

    public News(DboBetOpen betOpen, int lottery) {
        this.bootType = -1;
        this.title = MessageFormat.format(
                "恭喜{0}第{1}期中奖{2}元",
                betOpen.getNickName(), betOpen.getIssueName(), Moneys.format(betOpen.getWinMoney()));
        if (lottery == 0) {
            this.title = MessageFormat.format(
                    "恭喜{0}购买{1}中奖{2}元",
                    betOpen.getNickName(), betOpen.getLotteryName(), Moneys.format(betOpen.getWinMoney()));
        }
        this.bootContent = new Gson().toJson(betOpen);
        this.timeState = 2;
        this.lotteryWinNotice = true;
    }
}
