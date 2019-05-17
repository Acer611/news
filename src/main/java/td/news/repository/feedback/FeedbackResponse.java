package td.news.repository.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.JSONMapper;
import td.news.repository.feedback.vo.SchemeFeedbackCmd;

/**
 * @author sanlion do
 */
@Service
public class FeedbackResponse {

    @Autowired @Qualifier("redis124db1Template") private StringRedisTemplate redis124db1Template;

    public void gteCmd_feedbackByScheme(SchemeFeedbackCmd cmd) {
        String value = JSONMapper.toJson(cmd);
        redis124db1Template.opsForList().rightPush("cmd:feedback:byScheme", value);
    }
}
