package td.news.repository.message;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.message.dbo.CacheNotification_All;
import td.news.repository.message.dbo.CacheNotification_Multi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class NotificationRepository_Xiaomi {

    @Autowired @Qualifier("redis6399db2Template") private StringRedisTemplate db2;
    private static final int rangeSize = 500;

    /**
     * 获取最终消息（全平台推送）
     *
     * @return
     */
    public List<CacheNotification_All> rangeXiaomiAllNotification() {
        ListOperations<String, String> operations = db2.opsForList();
        String key = "n:final:Xiaomi:all";
        List<String> values = operations.range(key, 0, rangeSize - 1);
        if (Objects.isNull(values) || values.isEmpty()) {
            return Collections.emptyList();
        }
        operations.trim(key, rangeSize, -1);
        return values.stream().filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> new Gson().fromJson(it, CacheNotification_All.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取最终消息（全平台推送）
     *
     * @return
     */
    public List<CacheNotification_Multi> rangeXiaomiMultiNotification() {
        ListOperations<String, String> operations = db2.opsForList();
        String key = "n:final:Xiaomi:multi";
        List<String> values = operations.range(key, 0, rangeSize - 1);
        if (Objects.isNull(values) || values.isEmpty()) {
            return Collections.emptyList();
        }
        operations.trim(key, rangeSize, -1);
        return values.stream().filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> new Gson().fromJson(it, CacheNotification_Multi.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * 小米，全平台消息
     *
     * @param notifications
     */
    public void pushMiAllNotification(List<CacheNotification_All> notifications) {
        if (notifications.isEmpty()) {
            return;
        }
        db2.opsForList().rightPushAll(
                "n:final:Xiaomi:all",
                notifications.stream().map(it -> new Gson().toJson(it)).collect(Collectors.toList()));
    }

    /**
     * 小米，群发消息/单条消息
     *
     * @param notifications
     */
    public void pushMiMultiNotification(List<CacheNotification_All> notifications) {
        if (notifications.isEmpty()) {
            return;
        }
        db2.opsForList().rightPushAll(
                "n:final:Xiaomi:multi",
                notifications.stream().map(it -> new Gson().toJson(it)).collect(Collectors.toList()));
    }
}
