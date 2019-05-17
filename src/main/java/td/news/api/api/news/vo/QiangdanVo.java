package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.biz.match.domain.MatchResult;
import td.news.biz.news.domain.NewsQiangdan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Data
public class QiangdanVo {

    @ApiModelProperty("比赛编号") String no;
    @ApiModelProperty("周") String week;
    @ApiModelProperty("联赛") private String league;
    @ApiModelProperty("开场时间") private String begin;
    @ApiModelProperty("主队") private String homeName;
    @ApiModelProperty("客队") private String visitingName;
    @ApiModelProperty("主队LOGO") private String homeLogo;
    @ApiModelProperty("客队LOGO") private String visitorLogo;
    @ApiModelProperty("关联的资讯列表") private List<NewsArrayVo.NewsVo> newses = new ArrayList<>();

    @ApiModelProperty("提点") private String advice;
    @ApiModelProperty("推荐结果") private String adviceResult;
    @ApiModelProperty("推荐概率") private String adviceRate;

    // 以下为结果对比
    @ApiModelProperty("比赛结果") private String result;
    @ApiModelProperty("比赛比分") private String resultScore;
    @ApiModelProperty("状态（0.未开始 1.进行中 2.已结束）") int state;
    @ApiModelProperty("推荐是否命中") private boolean adviceHit;
    @ApiModelProperty("是否有资格查看") private boolean right;
    @ApiModelProperty("比赛场次号") private String issueNo;
    @ApiModelProperty("推荐的是否是让球") private boolean rq;
    @ApiModelProperty("胜平负打出概率") private List<String> spfRate = new ArrayList<>();
    @ApiModelProperty("让球胜平负打出概率") private List<String> rqspfRate = new ArrayList<>();

    public QiangdanVo(NewsQiangdan value) {
        this.no = value.getMatchNo();
        this.week = value.getWeek();
        this.league = value.getLeague();
//        this.begin = Times.format(value.getMatchTime(), Times.yyyyMMddHHmm);
        this.begin = value.getFormatMatchTime();
        this.homeName = value.getHome();
        this.visitingName = value.getVisitor();
        if (!value.getNewses().isEmpty()) {
            value.getNewses().forEach(it -> this.newses.add(new NewsArrayVo.NewsVo(it)));
        }
        this.advice = value.getAdvice();
        this.adviceResult = value.getAdviceResult().getKey();
        this.adviceRate = value.getAdviceRate();
        this.right = value.isRight();
        if (Objects.nonNull(value.getMatchResult())) {
            MatchResult matchResult = value.getMatchResult();
            this.result = matchResult.getResult();
            this.resultScore = matchResult.getResultScore();
            this.state = 2;
            this.adviceHit = value.getAdviceResult().getResult().contains(matchResult.getResult());
            this.right = true;
        }
        this.homeLogo = value.getHomeLogo();
        this.visitorLogo = value.getVisitorLogo();

        this.issueNo = value.getIssueNo();
        this.rq = value.isRq();
        this.spfRate.addAll(value.getSpfRate());
        this.rqspfRate.addAll(value.getRqspfRate());
    }

    @Data
    public static class QiangdanArrayVo {

        @ApiModelProperty("期次") private String issue;
        @ApiModelProperty("总推荐数") private int count;
        @ApiModelProperty("强胆、博胆推荐列表") private List<QiangdanVo> value = new ArrayList<>();
    }

    @Data
    public static class QiangdanArrayWrapVo{

        List<QiangdanArrayVo> wrap = new ArrayList<>();
    }
}
