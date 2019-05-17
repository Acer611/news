package td.news.biz.match.domain;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.NoArgsConstructor;
import td.news.client.match.ClientMatchArrayVo;
import td.news.common.Times;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class Match {

    private String issueNo; // 大期次中对应对阵的最小期次编号，能够唯一确定一场比赛的编号
    private String week;
    private String matchNo;
    private String league;
    private Date matchTime;
    private String homeTeamName;
    private String visitingTeamName;
    private int state; // 0未开始 1进行中 2已结束
    private int homeScore;
    private int visitorScore;
    private int homeHalfScore;
    private int visitorHalfScore;
    private String result;
    private int rq;
    private String rqResult;
    private boolean end;
    private String spspf;
    private String rqSpf;

    public Match() {
    }

    public Match(ClientMatchArrayVo.ClientMatchVo dboMatch) {

        this.issueNo = dboMatch.getIssueNo();
        this.week = dboMatch.getWKName();
        this.matchNo = dboMatch.getMNO();
        this.league = dboMatch.getLeagueName();
        this.matchTime = Times.parse(dboMatch.getMatchTime(), "yyyy/MM/dd HH:mm:ss");
        this.homeTeamName = dboMatch.getHTeam();
        this.visitingTeamName = dboMatch.getVTeam();
        this.state = Strings.isNullOrEmpty(dboMatch.getRz()) || "--".equals(dboMatch.getRz()) ? 0 : 2;
        this.end = !Strings.isNullOrEmpty(dboMatch.getRz()) && dboMatch.getRz().contains(":");
        this.spspf = dboMatch.getSPSPF();
        this.rqSpf = dboMatch.getSPRQS();
        // 处理比分
        if (dboMatch.getRz().contains(":")) {
            String[] split = dboMatch.getRz().split(":");
            this.homeScore = Integer.parseInt(split[0]);
            this.visitorScore = Integer.parseInt(split[1]);
            if (this.homeScore > this.visitorScore) {
                this.result = "主胜";
            }
            if (this.homeScore == this.visitorScore) {
                this.result = "平";
            }
            if (this.homeScore < this.visitorScore) {
                this.result = "主负";
            }
            if (!Strings.isNullOrEmpty(dboMatch.getRQ())) {
                this.rq = Integer.parseInt(dboMatch.getRQ());
                if (this.homeScore + this.rq > this.visitorScore) {
                    this.rqResult = "让胜";
                }
                if (this.homeScore + this.rq == this.visitorScore) {
                    this.rqResult = "让平";
                }
                if (this.homeScore + this.rq < this.visitorScore) {
                    this.rqResult = "让负";
                }
            }
        }
        if (!Strings.isNullOrEmpty(dboMatch.getHRz()) &&dboMatch.getHRz().contains(":")) {
            String[] split = dboMatch.getHRz().split(":");
            this.homeHalfScore = Integer.parseInt(split[0]);
            this.visitorHalfScore = Integer.parseInt(split[1]);
        }
    }
}
