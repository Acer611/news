package td.news.biz.news.domain;

import lombok.Data;
import td.news.mapper.TD_CP_News.DbComment;

import java.util.Date;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Data
public class NewsComment {

    private long id;
    private String content;
    private Date create;
    private News ref;
    String auditReply;
    int auditState;
    String avatar;
    String username;
    String nickname;

    public NewsComment(DbComment value) {
        this.id = value.getId();
        this.content = value.getContent();
        this.create = value.getCreate();
        if (Objects.nonNull(value.getNews())) {
            this.ref = new News(value.getNews());
        }
        this.auditReply = value.getAuditReply();
        this.auditState = value.getAuditState();
        this.username = value.getUserName();
        this.nickname = value.getUserNick();
        this.avatar = value.getUserAvatar();
    }
}
