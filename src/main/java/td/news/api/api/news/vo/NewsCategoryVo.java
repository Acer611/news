package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.biz.news.domain.NewsCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class NewsCategoryVo {

    @ApiModelProperty("编码") private String code;
    @ApiModelProperty("分类标题") private String name;

    public NewsCategoryVo(NewsCategory value) {
        this.code = value.getCode();
        this.name = value.getName();
    }

    @Data
    public static class ArrayVo {

        private List<NewsCategoryVo> categories = new ArrayList<>();
    }
}
