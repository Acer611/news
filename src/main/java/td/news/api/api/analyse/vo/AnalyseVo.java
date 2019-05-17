package td.news.api.api.analyse.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.repository.spider.dbo.DboAnalyse;

/**
 * @author sanlion do
 */
@Data
public class AnalyseVo {

    @ApiModelProperty("比赛分析页面链接") String originAnalyseDetailHref;
    @ApiModelProperty("历史交锋 场次数") int hisHitCount;
    @ApiModelProperty("历史交锋 胜,平,负") String historyScore;
    @ApiModelProperty("客队历史战绩 胜,平,负") String visitRecent;
    @ApiModelProperty("主队历史战绩 胜,平,负") String hostRecent;
    @ApiModelProperty("平均赔率 胜") String odds3;
    @ApiModelProperty("平均赔率 平") String odds1;
    @ApiModelProperty("平均赔率 负") String odds0;
    @ApiModelProperty("主队排名") String hostRank;
    @ApiModelProperty("客队排名") String visitorRank;

    public AnalyseVo(DboAnalyse analyse) {
        this.originAnalyseDetailHref = analyse.getLink();
        this.hisHitCount = analyse.getHisHitCount();
        this.historyScore = analyse.getHistoryScore();
        this.visitRecent = analyse.getVisitRecent();
        this.hostRecent = analyse.getHostRecent();
        this.odds3 = analyse.getOdds3();
        this.odds1 = analyse.getOdds1();
        this.odds0 = analyse.getOdds0();
        this.hostRank = analyse.getHostRankInfo();
        this.visitorRank = analyse.getVisitRankInfo();
    }
}
