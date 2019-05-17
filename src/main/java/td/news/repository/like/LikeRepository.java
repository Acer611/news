package td.news.repository.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * @author sanlion do
 */
@Service
public class LikeRepository {

    @Autowired @Qualifier("redis6399db7Template") private StringRedisTemplate db7;

    public boolean hasLike(int type, long target, long uid){
        String key = MessageFormat.format("link:like:{0}:{1}", String.valueOf(type), String.valueOf(target));
        return db7.opsForSet().isMember(key, String.valueOf(uid)).booleanValue();
    }
}
