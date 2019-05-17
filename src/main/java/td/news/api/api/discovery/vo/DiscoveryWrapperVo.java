package td.news.api.api.discovery.vo;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.api.api.news.vo.NewsArrayVo;
import td.news.api.api.news.vo.SimpleNewsCommentVo;
import td.news.common.NicknameConverter;
import td.news.common.Times;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Data
public class DiscoveryWrapperVo {

    @ApiModelProperty("0.资讯 1.晒单") int type = 0;
    NewsArrayVo.NewsVo news;
    ShareSchemeVo scheme;

    public DiscoveryWrapperVo(DbShareScheme dbShareScheme, long self) {
        this.type = 1;
        this.scheme = new ShareSchemeVo(dbShareScheme, self);
    }

    @Data
    public static class DiscoveryWrapperArrayVo {

        private List<DiscoveryWrapperVo> value = new ArrayList<>();
    }

    @Data
    public static class ShareSchemeArrayVo {

        List<ShareSchemeVo> value = new ArrayList<>();
    }

    @Data
    public static class ShareSchemeVo {

        @ApiModelProperty("晒单ID") long id;
        @ApiModelProperty("点赞数") private int likes;
        @ApiModelProperty("评论数") private int comments;
        @ApiModelProperty("是否点赞过") private boolean liked;
        @ApiModelProperty("内容") private String remark;
        SimpleNewsCommentVo.BaseUserVo author;
        @ApiModelProperty("晒单时间") String create;
        long schemeId;
        double betMoney;
        double betWonMoney;
        @ApiModelProperty("订单状态（5.中奖 6.未中奖）") int state;
        String issueName;
        int lottery;
        String lotteryName;
        @ApiModelProperty("彩种icon") String lotteryIcon;
        @ApiModelProperty("是否彩帝推荐方案") boolean diPlan;
        @ApiModelProperty("跟单人数") int betFollower;
        @ApiModelProperty("跟单金额") double followBetMoney;
        @ApiModelProperty("跟单中奖金额") double followBetWonMoney;
        @ApiModelProperty("晒单图片") String imgUrl;
        @ApiModelProperty("盈利率") String profitRate;
        @ApiModelProperty("是否精华") boolean jh;


        public ShareSchemeVo(DbShareScheme scheme, long self) {
            this.id = scheme.getID();
            this.likes = scheme.getLikes();
            this.comments = scheme.getComments();
            this.remark = NicknameConverter.convert(scheme.getRemark());
            this.create = Times.format(scheme.getCreateTime(), Times.MMddHHmm);
            this.schemeId = scheme.getSchemeID();
            this.betMoney = scheme.getMoney();
            this.betWonMoney = scheme.getWinMoney();
            this.state = scheme.getStateCode();
            this.issueName = scheme.getIssueName();
            this.lottery = scheme.getLotteryID();
            this.lotteryName = scheme.getLotteryName();
            this.lotteryIcon = scheme.getLotteryIcon();
            this.author = new SimpleNewsCommentVo.BaseUserVo(scheme.getUserID(), NicknameConverter.convert(scheme.getNickName()), scheme.getUserAvatar(), self);
            this.imgUrl = scheme.getImgUrl();
            this.profitRate = NumberFormat.getPercentInstance().format(scheme.getProfitRate());
            if (!Strings.isNullOrEmpty(scheme.getBizContent())) {
                DbShareScheme.BizContentValue bizContentValue = new Gson().fromJson(scheme.getBizContent(), DbShareScheme.BizContentValue.class);
                if (Objects.nonNull(bizContentValue)) {
                    this.betFollower = bizContentValue.getFollower();
                    this.diPlan = bizContentValue.isFromMaster();
                    this.followBetMoney = bizContentValue.getFollowBetMoney();
                    this.followBetWonMoney = bizContentValue.getFollowBetWonMoney();
                }
            }
            this.jh = scheme.isIsJh();
        }
    }
}
