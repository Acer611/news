package td.news.repository.mbr;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.JSONMapper;
import td.news.repository.mbr.dbo.DbBaseUserInfo;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static td.news.configuration.CacheConfiguration.bizCache_30_min;

/**
 * @author sanlion do
 */
@Service
public class MbrRepository {

    @Autowired
    @Qualifier("redis6399Template")
    private StringRedisTemplate template;

    @Cacheable(value = bizCache_30_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public DbBaseUserInfo getBaseUser(long uid) {
        String key = MessageFormat.format("User_GetUserBaseInfoByID:{0}", String.valueOf(uid));
        String value = template.opsForValue().get(key);
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return new Gson().fromJson(value, DbBaseUserInfo.class);
    }

    @Cacheable(value = bizCache_30_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbBaseUserInfo> multi(List<Long> uidArray) {
        List<String> values = template.opsForValue().multiGet(
                uidArray.stream().map(it -> MessageFormat.format("User_GetUserBaseInfoByID:{0}", String.valueOf(it))).collect(Collectors.toSet()));
        if (Objects.isNull(values) || values.isEmpty()) {
            return Collections.emptyList();
        }
        return values.stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbBaseUserInfo.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public double getMoneyLimit() {
        String value = template.opsForValue().get("set.limit:comment:moneybuy");
        return !Strings.isNullOrEmpty(value) ? Double.parseDouble(value) : 10;
    }
}
