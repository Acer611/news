package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class QiangdanIssueVo {

    private String issueName;

    @Data
    public static class QiangdanIssueArrayVo {

        @ApiModelProperty("往期期次") private List<String> value = new ArrayList<>();
    }
}
