package td.news.client.match;

import lombok.Data;

import java.util.List;

@Data
public class ClientMatchArrayVo {

    private int code;
    private String msg;
    private List<ClientMatchVo> data;

    @Data
    public static class ClientMatchVo {

        private String WKName;
        private String MNO;
        private String LeagueName;
        private String HTeam;
        private String VTeam;
        private String HRz;
        private String Rz;
        private String IssueName;
        private String MatchTime;
        private String IssueNo;
        private String RQ;
        private String SPSPF;
        private String SPRQS;
        private String BaseEndTime; // yyyy-MM-dd HH:mm:ss
    }
}
