package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class NewsCommentRequestBody {

    @ApiModelProperty("资讯id") long id;
    @ApiModelProperty("内容") String content;
}
