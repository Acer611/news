package td.news.api.api.banner.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.api.api.news.vo.NewsArrayVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class NewsBannerArrayVo {

    @ApiModelProperty("顶部") private List<NewsArrayVo.NewsVo> newses = new ArrayList<>();

}
