package td.news.biz.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.repository.device.OnlineDeviceRepository;
import td.news.repository.message.dbo.DboOnlineDevice;

import java.util.Objects;

@Service
public class OnlineDeviceService {

    @Autowired private OnlineDeviceRepository onlineDeviceRepository;

    public DboOnlineDevice device(String deviceCode) {
        return onlineDeviceRepository.device(deviceCode);
    }

    public DboOnlineDevice device(long uid) {
        return onlineDeviceRepository.device(uid);
    }

    public String userTag(long uid) {
        DboOnlineDevice device = device(uid);
        return Objects.isNull(device) ? "1" : userTag(device.getDeviceCode());
    }

    public String userTag(String deviceCode) {
        if(true){
            return "4";
        }
        DboOnlineDevice device = device(deviceCode);
        if (Objects.isNull(device)) {
            return "1";
        }
        // 1新设备、2无账号设备、3有账号无购彩设备、4有账号有购彩
        if (device.isActive() && device.isBet()) {
            return "4";
        }
        if (device.isActive() && !device.isBet()) {
            return "3";
        }
        if (!device.isActive()) {
            return "2";
        }
        return "1";
    }
}
