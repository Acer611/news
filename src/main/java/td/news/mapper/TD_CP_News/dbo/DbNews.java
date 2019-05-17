package td.news.mapper.TD_CP_News.dbo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class DbNews implements Serializable{

    long ID;
    String Title;
    int TypeID;
    String TypeName;
    String IconUrl;
    String LinkUrl;
    String Content;
    String Author;
    String Origin;
    int OrderNo;
    Date UpdateTime;
    Date CreateTime;
    Date StartTime;
    Date EndTime;
    String AppCode;
    String AppStore;
    String CityName;
    int ReadCount;
    int IsTop;
    int PushTypeID;
    String BootContent;
    String UserTags;
    String LotteryID; // 逗号分隔，可空，可多个

    String Code;
    String AppType;
    int ContentType;//资讯内容类型 0-富文本 1-视频
}
