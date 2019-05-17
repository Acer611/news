package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
@AllArgsConstructor
public class AppCodeVo {

    @ApiModelProperty int appCode;
    @ApiModelProperty("应用名称") String appName;
}
