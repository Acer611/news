package td.news.biz.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.common.AppType;
import td.news.common.DeviceType;
import td.news.repository.message.MessageRepository;
import td.news.repository.message.dbo.DboOnlineDevice;

/**
 * @author sanlion do
 */
@Service
public class MessageService {

    @Autowired private MessageRepository messageRepository;

    public void logOnline(
            String token,
            long uid, int appCode, String brand,
            DeviceType type, String bundle, String deviceCode, String pushDeviceCode, String mobileCode, String channel, String version,
            AppType appType) {
        DboOnlineDevice device = new DboOnlineDevice();
        device.setPushDeviceCode(pushDeviceCode);
        device.setDeviceCode(deviceCode);
        device.setType(type);
        device.setUid(uid);
        device.setAppCode(appCode);
        device.setBundle(bundle);
        device.setToken(token);
        device.setMobileCode(mobileCode);
        device.setChannel(channel);
        device.setVersion(version);
        device.setBrand(brand);
        device.setAppType(appType);
        messageRepository.maintainOnlineDevice(device);
    }

}
