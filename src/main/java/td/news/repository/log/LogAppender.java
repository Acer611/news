package td.news.repository.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;
import td.news.common.JSONMapper;
import td.news.common.ServerHelper;
import td.news.configuration.AppCtx;


public class LogAppender extends AppenderBase<ILoggingEvent> {

    private static String ip;

    static {
        ip = ServerHelper.ip();
    }

    @Setter String key;

    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }
        String appName = AppCtx.getApplicationContext().getEnvironment().getProperty("spring.application.name");
        StringRedisTemplate log = AppCtx.getApplicationContext().getBean("redis6399db0logTemplate", StringRedisTemplate.class);
        LogCommand logCommand = new LogCommand();
        logCommand.setAppName(appName);
        logCommand.setContent(event.getFormattedMessage());
        logCommand.setLevel(event.getLevel().toString());
        logCommand.setIp(ip);
        try {
            log.opsForList().rightPush(key, JSONMapper.toJson(logCommand));
        } catch (Exception e) {

        }

    }


}
