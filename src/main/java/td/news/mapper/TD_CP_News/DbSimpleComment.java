package td.news.mapper.TD_CP_News;

import lombok.Data;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class DbSimpleComment {

    private long ID;
    private String Comment;
    private Date CreateTime;
    private long UserID;
    String UserName;
    String UserAvatar;
    String UserNick;
    private String AuditReply; // 审核回复
    private int AuditState = 1; // 审核状态 0.未审核 1.审核通过 2.审核拒绝
}
