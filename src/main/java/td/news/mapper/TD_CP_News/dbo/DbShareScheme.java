package td.news.mapper.TD_CP_News.dbo;

import lombok.Data;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class DbShareScheme {

    long ID;
    int Likes;
    int Comments;
    long UserID;
    String UserName;
    String NickName;
    String UserAvatar;
    String Remark;
    Date CreateTime;
    String BizContent;
    String ImgUrl;
    int IsTop;
    Date TopTime;
    boolean IsJh;
    Date JhTime;

    private long SchemeID;
    private double Money;
    private double WinMoney;
    private int StateCode; // // 1.撤单 2.等待付款 3.等待出票 4.等待开奖 5.已中奖 6.未中奖 7.等待派奖
    private long IssueID;
    private String IssueName;
    private int LotteryID;
    String LotteryName;
    String LotteryIcon;
    private int RecommendType;  // 1 公开，2 保密;
    private boolean FromMaster;
    double ProfitRate;


    @Data
    public static class BizContentValue {

        int follower;
        boolean fromMaster;
        double followBetMoney;
        double followBetWonMoney;
    }
}
