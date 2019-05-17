package td.news.mapper.TD_CP_News;

import lombok.Data;
import td.news.mapper.TD_CP_News.dbo.DbNews;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class DbComment {

    private long id;
    private String content;
    private Date create;
    private DbNews news;
    private String AuditReply; // 审核回复
    private int AuditState = 1; // 审核状态 0.未审核 1.审核通过 2.审核拒绝
    private String UserNick;
    private String UserAvatar;
    private String UserName;
}
