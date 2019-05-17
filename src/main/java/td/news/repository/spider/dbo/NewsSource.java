package td.news.repository.spider.dbo;

import lombok.Getter;

/**
 * @author sanlion do
 */
@Getter
public enum NewsSource {

    jingcaizhuanjia(15,10031, "竞彩专家", "中国竞彩网"),
    jingzuzixun(16,10032, "竞足资讯", "中国竞彩网"),
    jingcaichangwai(17,10033, "竞彩场外", "中国竞彩网"),
    lanqiuzixun(18,10034, "篮球资讯", "中国竞彩网"),
    shuangseqiuyuce(19,10035, "双色球预测", "网易"),
    daletouyuce(20,10036, "大乐透预测", "网易"),
    shijiebei(26,10037, "世界杯专题", "新浪")
;
    private int id;
    private int type;
    private String typeName;
    private String source;

    NewsSource(int id, int type, String typeName, String source) {
        this.id = id;
        this.type = type;
        this.typeName = typeName;
        this.source = source;
    }
}
