package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class ComplainRequestBody {

    @ApiModelProperty("举报对象ID") long target;
    @ApiModelProperty("举报内容") String content;
    @ApiModelProperty("举报类型") int targetType;
}
