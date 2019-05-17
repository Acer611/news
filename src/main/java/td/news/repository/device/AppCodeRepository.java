package td.news.repository.device;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.configuration.CacheConfiguration;
import td.news.repository.device.dbo.CacheAppCode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class AppCodeRepository {

    @Autowired
    @Qualifier("redis6399Template")
    private StringRedisTemplate template;

    @Cacheable(value = CacheConfiguration.bizCache_60_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<CacheAppCode> listAll() {
        String value = template.opsForValue().get("Base_AppTypeAll");
        if (Strings.isNullOrEmpty(value)) {
            return Collections.emptyList();
        }
        List<CacheAppCode> appCodes = new Gson().fromJson(
                value, new TypeToken<ArrayList<CacheAppCode>>() {
                }.getType());
        if (appCodes.isEmpty()) {
            return Collections.emptyList();
        }
        return appCodes.stream().filter(Objects::nonNull)
                .filter(it -> it.getState() == 1)
                .sorted(Comparator.comparingInt(CacheAppCode::getID))
                .collect(Collectors.toList());
    }

    public CacheAppCode getByAppCode(int appCode) {
        Optional<CacheAppCode> any = listAll().stream().filter(it -> appCode == it.getID()).findAny();
        return any.orElse(CacheAppCode.defaultAppCode);
    }

}
