package td.news.repository.comment;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.NicknameConverter;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class CommentRepository {

    @Autowired @Qualifier("redis6399db7Template") private StringRedisTemplate db7;


    public String md5(String content, long uid) {
        content = NicknameConverter.clear(content);
        return Hashing.md5().newHasher().putString(content, Charset.defaultCharset()).putLong(uid).hash().toString();
    }

    public boolean repeat(String content, long uid) {
        if (content.length() <= 3) {
            return false;
        }
        return db7.hasKey(MessageFormat.format("comment:news_repeat:{0}", md5(content, uid))).booleanValue();
    }

    public void logRepeat(long uid, String content) {
        db7.opsForValue().set(MessageFormat.format("comment:news_repeat:{0}", md5(content, uid)), content, 5, TimeUnit.MINUTES);
    }
}
