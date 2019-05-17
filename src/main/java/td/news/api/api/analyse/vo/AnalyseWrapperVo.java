package td.news.api.api.analyse.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sanlion do
 */
@Data
public class AnalyseWrapperVo {

    private Map<String, AnalyseVo> analyses = new HashMap<>();
}
