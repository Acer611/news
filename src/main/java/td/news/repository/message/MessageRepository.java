package td.news.repository.message;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import td.news.common.AppType;
import td.news.repository.device.OnlineDeviceRepository;
import td.news.repository.message.dbo.DboOnlineDevice;
import td.news.repository.message.dbo.NewDboPushCert;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class MessageRepository {


    @Autowired @Qualifier("redis6399db1Template") private StringRedisTemplate db1;
    @Autowired private OnlineDeviceRepository onlineDeviceRepository;
    @Autowired @Qualifier("redis124db1Template") private StringRedisTemplate redis124db1Template;

    public void maintainOnlineDevice(DboOnlineDevice device) {
        /*
        直接放入待处理队列，等待调度慢处理
         */
        onlineDeviceRepository.pushUntreatedDevice(device);
    }


    public void saveDevice(DboOnlineDevice device) {
        ValueOperations<String, String> ops = db1.opsForValue();
        String newOnlineDeviceJson = new Gson().toJson(device);
        ops.set(MessageFormat.format("online:device:{0}", device.getDeviceCode()), newOnlineDeviceJson);
        // 仅当用户登陆时，才记录
        if (0 != device.getUid()) {
            ops.set(MessageFormat.format("online:user:{0}", String.valueOf(device.getUid())), newOnlineDeviceJson);
        }
    }

    public void updatePushTag(long uid, List<String> tags_add, AppType appType) {
        DboOnlineDevice device = onlineDeviceRepository.device(uid);
        if (device == null) {
            return;
        }
        NewDboPushCert cert = getCert(device.getAppCode(), device.getBrand(), appType);
        if (cert == null) {
            return;
        }
        device.setPushTag(tags_add);
        saveDevice(device);
        maintainOnlineDevice(device);
    }

    public List<NewDboPushCert> certs(AppType appType) {
        List<String> members = redis124db1Template.opsForList().range(MessageFormat.format("push:cert:{0}", appType), 0, -1);
        if (Objects.isNull(members) || members.isEmpty()) {
            return Collections.emptyList();
        }
        return members.stream().map(it -> new Gson().fromJson(it, NewDboPushCert.class)).filter(it -> !it.isInactive()).collect(Collectors.toList());
    }

    public NewDboPushCert getCert(int appCode, String brand, AppType appType) {
        return certs(appType).stream()
                .filter(it ->
                        it.getAppCode() == appCode
                                && it.getBrand().equalsIgnoreCase(brand))
                .findAny().orElse(null);
    }
}
