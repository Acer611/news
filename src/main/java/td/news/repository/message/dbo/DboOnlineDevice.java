package td.news.repository.message.dbo;

import lombok.Data;
import lombok.NoArgsConstructor;
import td.news.common.AppType;
import td.news.common.DeviceType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
@NoArgsConstructor
public class DboOnlineDevice {

    private long uid;
    private int appCode;
    private String brand;
    private DeviceType type;
    private String bundle;
    private String deviceCode;
    private String pushDeviceCode;
    private boolean fresh;
    private boolean bet;
    private boolean active;
    private String token;
    private String mobileCode;
    private String channel;
    private String version;
    private String username;

    // edited by sanlion at 2018/2/5
    private List<String> pushTag = new ArrayList<>();

    // 翻页属性
    private Date pageDate;
    private boolean sameDevicePushDevice;
    private NewDboPushCert cert;
    private AppType appType;
}
