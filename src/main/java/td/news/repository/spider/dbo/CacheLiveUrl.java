package td.news.repository.spider.dbo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
@AllArgsConstructor
public class CacheLiveUrl {

    String issueNo;
    String liveUrl;
    String referer;
    String liveUrl4H5;
}
