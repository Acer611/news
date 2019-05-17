package td.news.api.api.news.vo;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import td.news.biz.news.domain.News;
import td.news.common.Times;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class NewsArrayVo {

    @ApiModelProperty private List<NewsVo> newses = new ArrayList<>();

    @Data
    public static class NewsVo {

        @ApiModelProperty("id") private long id;
        @ApiModelProperty("标题") private String title;
        @ApiModelProperty("创建时间") private String create;
        @ApiModelProperty("更新时间") private String update;
        @ApiModelProperty("封面/标题") private String cover;
        @ApiModelProperty("跳转") private String link;
        @ApiModelProperty("开始时间") String begin;
        @ApiModelProperty("结束时间") String end;
        @ApiModelProperty("时间状态，1.未开始 2.进行中 3.已结束") int timeState;
        @ApiModelProperty("作者") private String author;
        @ApiModelProperty String source;
        @ApiModelProperty("来源") private String origin;
        @ApiModelProperty("内容") private String content;
        @ApiModelProperty("引导内容JSON") private String bootContent;
        @ApiModelProperty("引导类型（仅不为7的时候生效）") int bootType;
        @ApiModelProperty("是否中奖通知") private boolean lotteryWinNotice;
        @ApiModelProperty("浏览数") private int readCount;
        @ApiModelProperty("资讯内容类型，0-富文本，1-视频") private int contentType;

        public NewsVo() {
        }

        public NewsVo(News value) {

            this.id = value.getId();
            this.title = value.getTitle();
            this.update = Times.format(value.getUpdate(), Times.yyyyMMddHHmm);
            this.create = Times.format(value.getCreate(), Times.yyyyMMddHHmm);
            this.cover = value.getIcon();
            this.link = value.getLink();
            this.begin = Times.format(value.getBegin(), Times.yyyyMMddHHmm);
            this.end = Times.format(value.getEnd(), Times.yyyyMMddHHmm);
            this.timeState = value.getTimeState();
            this.author = Strings.isNullOrEmpty(value.getAuthor()) ? "懂彩帝" : value.getAuthor();
            this.source = value.getSource();
            this.origin = value.getOrigin();
            this.content = value.getContent();
            this.bootContent = value.getBootContent();
            this.bootType = value.getBootType() == 0 ? 7 : value.getBootType();
            this.lotteryWinNotice = value.isLotteryWinNotice();
            this.readCount = value.getReadCounts();
            this.contentType=value.getContentType();
        }
    }
}
