package td.news.repository.spider.dbo;

import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class DboAnalyse {

    String issueNO;
    String originMatchID; // 原始比赛ID
    int hisHitCount; // 历史交锋 场次数
    String historyScore; // 历史交锋 胜,平,负
    String visitRecent; // 客队历史战绩 胜,平,负
    String hostRecent; // 主队历史战绩 胜,平,负
    String odds3; // 平均赔率 胜
    String odds1; // 平均赔率 平
    String odds0; // 平均赔率 负
    String originLetoulaMathID;
    String link;
    String netOriginLink;
    String letoulaOriginLink;
    String visitRankInfo;
    String hostRankInfo;
}
