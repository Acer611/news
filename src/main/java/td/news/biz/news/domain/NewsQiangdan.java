package td.news.biz.news.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import td.news.biz.match.domain.MatchResult;
import td.news.repository.spider.dbo.AdviceKeyword;
import td.news.repository.spider.dbo.DboQiangdan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Data
public class NewsQiangdan implements Serializable{

    private String home;
    private String homeLogo;
    private String league;
    private String matchNo;
    private String week;
    private String visitor;
    private String visitorLogo;
    private String advice;
    private AdviceKeyword adviceResult;
    private String adviceRate;
    private Date matchTime;
    private String formatMatchTime;
    private List<News> newses = new ArrayList<>();
    private List<String> ref = new ArrayList<>();
    private List<String> resultAnalysis = new ArrayList<>();
    private boolean rq;
    private MatchResult matchResult;
    private String issueNo;
    private boolean right;
    private List<String> spfRate = new ArrayList<>();
    private List<String> rqspfRate = new ArrayList<>();

    public NewsQiangdan() {
    }

    public NewsQiangdan(DboQiangdan value) {
        this.home = value.getHome();
        this.homeLogo = value.getHomeLogo();
        this.league = value.getLeague();
        this.matchNo = value.getMatchNo();
        this.week = value.getWeek();
        this.visitor = value.getVisitor();
        this.visitorLogo = value.getVisitorLogo();
        this.advice = value.getAdvice();
        this.adviceResult = value.getAdviceResult();
        this.adviceRate = value.getRate();
        this.matchTime = value.getDate();
        if (Objects.nonNull(value.getRef()) || !value.getRef().isEmpty()) {
            this.ref.addAll(value.getRef());
        }
        this.rq = value.isRq();
        this.formatMatchTime = value.getFormatDate();
        this.issueNo = value.getIssueNo();
    }
}
