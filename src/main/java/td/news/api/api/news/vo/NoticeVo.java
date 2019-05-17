package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.biz.news.domain.News;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class NoticeVo {

    @ApiModelProperty("1.公告 2.中奖通知") int type = 1;
    @ApiModelProperty("标题") String title;
    @ApiModelProperty("内容") String bootContent;

    public NoticeVo(News value) {
        if (value.isLotteryWinNotice()) {
            this.type = 2;
        }
        this.title = value.getTitle();
        this.bootContent = value.getBootContent();
    }

    @Data
    public static class NoticeArrayVo{

        List<NoticeVo> notices = new ArrayList<>();
    }
}
