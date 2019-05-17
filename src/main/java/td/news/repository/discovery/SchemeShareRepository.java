package td.news.repository.discovery;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.JSONMapper;
import td.news.mapper.TD_CP_News.ShareSchemeMapper;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class SchemeShareRepository {

    @Autowired private ShareSchemeMapper shareSchemeMapper;
    @Autowired @Qualifier("redis6399db7Template") private StringRedisTemplate db7;

    public List<DbShareScheme> pageNormal(long uid, int page, int pageSize) {
        Set<String> values = db7.opsForZSet().reverseRange(MessageFormat.format("scheme_share:normal:{0}", String.valueOf(uid)), (page - 1) * pageSize, page * pageSize - 1);
        if (values.isEmpty()) {
            return Collections.emptyList();
        }
        return multi(values);
    }

    public List<DbShareScheme> page(long uid, int page, int pageSize) {
        Set<String> values = db7.opsForZSet().reverseRange(MessageFormat.format("scheme_share:all:{0}", String.valueOf(uid)), (page - 1) * pageSize, page * pageSize - 1);
        if (values.isEmpty()) {
            return Collections.emptyList();
        }
        return multi(values);
    }

    private List<DbShareScheme> multi(Set<String> target) {
        HashOperations<String, String, String> hash = db7.opsForHash();
        return hash.multiGet("scheme_share:detail", target).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbShareScheme.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    private List<DbShareScheme> multi(List<String> target) {
        HashOperations<String, String, String> hash = db7.opsForHash();
        return hash.multiGet("scheme_share:detail", target).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbShareScheme.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbShareScheme> page(int page, int pageSize) {
        List<String> value = new ArrayList<>();
        if (page == 1) {
            Set<String> topValue = db7.opsForZSet().reverseRange("scheme_share:top", 0, -1);
            if (!topValue.isEmpty()) {
                value.addAll(new ArrayList<>(topValue));
            }
        }
        Set<String> values = db7.opsForZSet().reverseRange("scheme_share:one_month", (page - 1) * pageSize, page * pageSize - 1);
        if (!values.isEmpty()) {
            value.addAll(new ArrayList<>(values));
        }
        if (value.isEmpty()) {
            return Collections.emptyList();
        }
        return multi(value);
    }


    public DbShareScheme get(long id) {
        HashOperations<String, String, String> hash = db7.opsForHash();
        DbShareScheme dbShareScheme = JSONMapper.fromJson(hash.get("scheme_share:detail", String.valueOf(id)), DbShareScheme.class);
        if (Objects.nonNull(dbShareScheme)) {
            return dbShareScheme;
        }
        dbShareScheme = shareSchemeMapper.get(id);
        if (Objects.nonNull(dbShareScheme)) {
            hash.put("scheme_share:detail", String.valueOf(id), JSONMapper.toJson(dbShareScheme));
        }
        return dbShareScheme;
    }


}
