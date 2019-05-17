package td.news.repository.notification.dbo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class CacheOriginMessage {

    String title;
    String content;
    String bootContent;
    String alias;
    String tags;
    /**
     * 0.全部 1.标签 2.别名 4.指定人群
     */
    int pushType;
    String pushTime;
    String endTime;
    String createTime;
    Date push;
    Date end;
    Date create;

    /**
     * 0.普通 1.彩帝发单提醒
     */
    int bizType;
    String bizValue;
    /**
     * 对应关联表
     */
    List<Long> targets = new ArrayList<>();
}
