package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.biz.news.domain.NewsComment;
import td.news.common.Times;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Data
public class NewsCommentVo {

    @ApiModelProperty("关联的资讯") private NewsArrayVo.NewsVo ref;
    @ApiModelProperty("评论时间") private String date;
    @ApiModelProperty("评论内容") private String content;
    @ApiModelProperty("评论ID") private long id;
    @ApiModelProperty("审核回复") private String auditReply;
    @ApiModelProperty("审核状态（0.未审核 1.审核通过 2.审核拒绝）") private int auditState;

    public NewsCommentVo(NewsComment value) {
        this.id = value.getId();
        this.content = value.getContent();
        this.date = Times.format(value.getCreate(), Times.yyyyMMddHHmmss);
        if (Objects.nonNull(value.getRef())) {
            this.ref = new NewsArrayVo.NewsVo(value.getRef());
        }
        this.auditReply = value.getAuditReply();
        this.auditState = value.getAuditState();
    }

    @Data
    public static class NewsCommentArrayVo {

        private List<NewsCommentVo> value = new ArrayList<>();
    }
}
