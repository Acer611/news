package td.news.api.api.analyse.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
@AllArgsConstructor
public class MatchLiveVo {

    @ApiModelProperty("动画直播URL") String url;
    @ApiModelProperty("动画直播URL-H5") String h5Url;
    @ApiModelProperty("referer") String referer;
}
