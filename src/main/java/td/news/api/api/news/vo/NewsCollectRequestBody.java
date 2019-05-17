package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class NewsCollectRequestBody {

    @ApiModelProperty("待收藏的资讯ID") long id;
}
