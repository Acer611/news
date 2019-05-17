package td.news.repository.message.dbo;

import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class CachePushChannel {

    String channel;
    String pkg;
    String appId;
    String appKey;
    String appSecret;

    public static final String Brand_Android_Default = "JPush";
    public static final String Brand_Xiaomi = "Xiaomi";
}
