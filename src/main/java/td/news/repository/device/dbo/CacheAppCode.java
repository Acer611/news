package td.news.repository.device.dbo;

import lombok.Builder;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
@Builder
public class CacheAppCode {

    private int ID;
    private String AppName;
    private int State; // 1.有效

    public static final CacheAppCode defaultAppCode
            = CacheAppCode.builder().ID(3).AppName("彩民彩票").build();
}
