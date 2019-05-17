package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.biz.news.domain.SimpleComment;
import td.news.common.Times;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class SimpleNewsCommentVo {


    @ApiModelProperty("评论时间") private String date;
    @ApiModelProperty("评论内容") private String content;
    @ApiModelProperty("评论ID") private long id;
    @ApiModelProperty("审核回复") private String auditReply;
    @ApiModelProperty("审核状态（0.未审核 1.审核通过 2.审核拒绝）") private int auditState;
    @ApiModelProperty("用户基本信息") private BaseUserVo user;

    public SimpleNewsCommentVo(SimpleComment value, long currentUid) {
        this.id = value.getId();
        this.content = value.getContent();
        this.date = Times.format(value.getCreate(), Times.yyyyMMddHHmmss);
        this.auditReply = value.getAuditReply();
        this.auditState = value.getAuditState();
        this.user = new BaseUserVo(value.getUid(), value.getNickname(), value.getAvatar(), currentUid);
    }

    @Data
    public static class SimpleNewsCommentArrayVo {

        private List<SimpleNewsCommentVo> value = new ArrayList<>();
    }

    @Data
    public static class BaseUserVo {
        @ApiModelProperty("用户ID") long uid;
        @ApiModelProperty("用户昵称") String nick;
        @ApiModelProperty("用户头像") String avatar;
        @ApiModelProperty("是否当前用户") boolean self;
        @ApiModelProperty("是否自己") boolean zj;

        public BaseUserVo(long uid, String nick, String avatar, long self) {
            this.uid = uid;
            this.nick = nick;
            this.avatar = avatar;
            this.self = self == uid;
            this.zj = this.self;
        }
    }
}
