package td.news.repository.notification;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.notification.dbo.CacheOriginMessage;
import td.news.repository.notification.dbo.MessageRpt;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class NotificationRepository {

    @Autowired @Qualifier("redis6399db2Template") private StringRedisTemplate db2;
    private static final int rangeSize = 100;


    /**
     * 从原始消息队列拉取最新N条消息
     *
     * @return
     */
    public List<CacheOriginMessage> rangeOriginMessage() {
        ListOperations<String, String> operations = db2.opsForList();
        String queue = queue(new Date());
        List<String> values = operations.range(
                queue, 0, rangeSize - 1);
        if (Objects.isNull(values) || values.isEmpty()) {
            return Collections.emptyList();
        }
        operations.trim(queue, rangeSize, -1);
        return values.stream().filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> new Gson().fromJson(it, CacheOriginMessage.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 插入原始消息
     *
     * @param originMessage
     */
    public void originPush(CacheOriginMessage originMessage) {
        Date finalPushTime = Objects.isNull(originMessage.getPush()) ? new Date() : originMessage.getPush();
        db2.opsForList().rightPush(queue(finalPushTime), new Gson().toJson(originMessage));
    }

    private String queue(Date pushTime) {
        LocalDateTime pushDateTime = LocalDateTime.ofInstant(pushTime.toInstant(), ZoneId.systemDefault());
        String queue = MessageFormat.format(
                "n:origin:{0}", pushDateTime.format(DateTimeFormatter.ofPattern("yyMMddHH")));
        return queue;
    }

    public void arriveCallback(long id) {
        HashOperations<String, String, String> hash = db2.opsForHash();
        MessageRpt messageRpt = new Gson().fromJson(hash.get("n:rpt", String.valueOf(id)), MessageRpt.class);
        messageRpt.setArrive(messageRpt.getArrive() + 1);
        hash.put("n:rpt", String.valueOf(id), new Gson().toJson(messageRpt));
    }

    public void clickCallback(long id) {
        HashOperations<String, String, String> hash = db2.opsForHash();
        MessageRpt messageRpt = new Gson().fromJson(hash.get("n:rpt", String.valueOf(id)), MessageRpt.class);
        messageRpt.setClick(messageRpt.getClick() + 1);
        hash.put("n:rpt", String.valueOf(id), new Gson().toJson(messageRpt));
    }

    public List<MessageRpt> allMessageRpt() {
        HashOperations<String, String, String> hash = db2.opsForHash();
        return hash.values("n:rpt").stream()
                .map(it -> new Gson().fromJson(it, MessageRpt.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }
}
