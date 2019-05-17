package td.news.biz.news.domain;

import lombok.Data;
import td.news.biz.Mbr.domain.BaseUserInfo;
import td.news.mapper.TD_CP_News.DbSimpleComment;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class SimpleComment {

    private long id;
    private long uid;
    private String content;
    private Date create;
    String auditReply;
    int auditState;
    String username;
    String nickname;
    String avatar;

    public SimpleComment(DbSimpleComment value) {
        this.id = value.getID();
        this.uid = value.getUserID();
        this.content = value.getComment();
        this.create = value.getCreateTime();
        this.auditReply = value.getAuditReply();
        this.auditState = value.getAuditState();
        this.username = value.getUserName();
        this.nickname = value.getUserNick();
        this.avatar = value.getUserAvatar();
    }
}
