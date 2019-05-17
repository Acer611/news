package td.news.repository.device;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import td.news.common.JSONMapper;
import td.news.repository.message.MessageRepository;
import td.news.repository.message.dbo.DboOnlineDevice;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OnlineDeviceRepository {

    @Autowired @Qualifier("redis6399Template") private StringRedisTemplate db0;
    @Autowired @Qualifier("redis6399db1Template") private StringRedisTemplate db1;
    @Autowired @Qualifier("redis6399db7Template") private StringRedisTemplate db7;
    @Autowired private MessageRepository messageRepository;

    public DboOnlineDevice device(String deviceCode) {
        if (Strings.isNullOrEmpty(deviceCode)) {
            return null;
        }
        String value = db1.opsForValue().get(MessageFormat.format("online:device:{0}", deviceCode));
        return !Strings.isNullOrEmpty(value) ? new Gson().fromJson(value, DboOnlineDevice.class) : null;
    }

    public DboOnlineDevice device(long uid) {
        if (uid == 0) {
            return null;
        }
        String value = db1.opsForValue().get(MessageFormat.format("online:user:{0}", String.valueOf(uid)));
        return !Strings.isNullOrEmpty(value) ? new Gson().fromJson(value, DboOnlineDevice.class) : null;
    }


    public Date getPageDate(String deviceCode, boolean reset){
        DboOnlineDevice device = device(deviceCode);
        if (Objects.isNull(device)) {
            return new Date();
        }
        if (reset) {
            Date pageDate = new Date();
            device.setPageDate(pageDate);
            save(device);
            return pageDate;
        }
        return device.getPageDate();
    }

    private void save(DboOnlineDevice device){
        ValueOperations<String, String> ops = db1.opsForValue();
        String newOnlineDeviceJson = new Gson().toJson(device);
        ops.set(MessageFormat.format("online:device:{0}", device.getDeviceCode()), newOnlineDeviceJson, 30, TimeUnit.DAYS);
        if (0 != device.getUid()) {
            ops.set(MessageFormat.format("online:user:{0}", String.valueOf(device.getUid())), newOnlineDeviceJson, 30, TimeUnit.DAYS);
        }
    }

    public void pushUntreatedDevice(DboOnlineDevice dboOnlineDevice){
        if (Objects.isNull(dboOnlineDevice)) {
            return;
        }
        db7.opsForList().rightPush("device:untreated", JSONMapper.toJson(dboOnlineDevice));
    }
}
