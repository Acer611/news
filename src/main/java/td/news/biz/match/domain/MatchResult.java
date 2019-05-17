package td.news.biz.match.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sanlion do
 */
@Data
public class MatchResult {

    private String result;
    private String resultScore;

    public MatchResult() {
    }

    public MatchResult(String result, int homeScore, int visitorScore) {

        this.result = result;
        this.resultScore = homeScore + ":" + visitorScore;
    }
}
