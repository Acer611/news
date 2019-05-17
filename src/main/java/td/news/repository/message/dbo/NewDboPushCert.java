package td.news.repository.message.dbo;

import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class NewDboPushCert {

    private int appCode;
    private boolean inactive;
    private String pkg;
    private String brand;
    private String appId;
    private String appKey;
    private String appSecret;
    private boolean iosSupport;
    private boolean androidSupport;

    public static final String Brand_Android_Default = "JPush";
    public static final String Brand_Xiaomi = "Xiaomi";
}
