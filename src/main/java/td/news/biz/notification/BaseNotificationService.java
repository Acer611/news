package td.news.biz.notification;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import td.news.common.Times;
import td.news.repository.notification.NotificationRepository;
import td.news.repository.notification.dbo.CacheOriginMessage;

import java.util.List;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Service
public class BaseNotificationService {


    @Autowired private NotificationRepository notificationRepository;

    /**
     * 入库
     *
     * @param message
     */
    @Async
    public void cache(CacheOriginMessage message) {
        if (Objects.isNull(message)) {
            return;
        }
        // 处理时间
        if (Objects.isNull(message.getCreate()) && !Strings.isNullOrEmpty(message.getCreateTime())) {
            message.setCreate(Times.parse(message.getCreateTime(), Times.yyyyMMddHHmmss));
        }
        if (Objects.isNull(message.getPush()) && !Strings.isNullOrEmpty(message.getPushTime())) {
            message.setPush(Times.parse(message.getPushTime(), Times.yyyyMMddHHmmss));
        }
        if (Objects.isNull(message.getEndTime()) && !Strings.isNullOrEmpty(message.getEndTime())) {
            message.setEnd(Times.parse(message.getEndTime(), Times.yyyyMMddHHmmss));
        }
        notificationRepository.originPush(message);
    }

    public void cache(List<CacheOriginMessage> messages) {
        if (messages.isEmpty()) {
            return;
        }
        // 处理时间
        messages.forEach(this::cache);
    }
}
