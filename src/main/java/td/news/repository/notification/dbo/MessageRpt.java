package td.news.repository.notification.dbo;

import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class MessageRpt {

    long id;
    String title;
    String content;
    int send;
    int success;
    int arrive;
    int click;
}
