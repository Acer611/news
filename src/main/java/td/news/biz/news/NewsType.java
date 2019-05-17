package td.news.biz.news;

import lombok.Getter;

/**
 * @author sanlion do
 */
public enum NewsType {

    banner_index("1001"),
    announcement("1002"),
    news("1003"),
    help("1004"),
    activity("1006"),
    launch("1007"),
    banner_news("1008"),
    banner_index_middle("10011"),
    banner_index_bottom("10012"),
    pop("10010"),
    banner_di("10013"),
    banner_bet("1009"), // 投注页banner
    banner_discovery_category("10040"), // 发现频道
    banner_discovery("10050"), // 发现频道广告
    banner_my("10051"),
    anno_im("10021"), // 聊天室公告
    banner_shijiebei("10025"), // 世界杯
    banner_live("10028"), // 直播banner
    banner_live_activity("10024"), // 彩帝直播间活动
    banner_shop("10029") //彩店联盟首页
    ;

    @Getter private String code;

    NewsType(String code) {
        this.code = code;
    }
}
