package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class NewsRemoveRequestBody {

    @ApiModelProperty("评论ID") long id;
}
