package td.news.repository.message.dbo;

import lombok.Data;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class CacheNotification_Base {

    String title;
    String content;
    String value;
    CachePushChannel channel;
    Date deadline;
}
