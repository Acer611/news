package td.news.repository.mbr;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.BizException;

import java.text.MessageFormat;

/**
 * Created by sanlion on 2017/5/25.
 */
@Service
public class TokenRepository {

    @Autowired @Qualifier("tokenOpt") private StringRedisTemplate tokenOpt;
    public static final BizException tokenEmptyBizException = new BizException(17031701);
    public static final BizException tokenExpireBizException = new BizException(17031702, "您的登录状态已过期，请重新登录");

    public long uid(String token) {
        if (Strings.isNullOrEmpty(token)) {
            throw tokenEmptyBizException;
        }
        String key = MessageFormat.format("User_GetUserIDByGuid:{0}", token);
        String value = tokenOpt.opsForValue().get(key);
        if (Strings.isNullOrEmpty(value)) {
            throw tokenExpireBizException;
        }
        return Integer.parseInt(value);
    }
}
