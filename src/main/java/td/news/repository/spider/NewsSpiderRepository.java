package td.news.repository.spider;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.spider.dbo.DboQiangdan;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class NewsSpiderRepository {

    @Autowired
    @Qualifier("redis6399Template")
    private StringRedisTemplate template;


    public List<DboQiangdan> listByDay(int dayOfYear) {
        String key = MessageFormat.format("news:qiangdan:day{0}", dayOfYear);
        HashOperations<String, String, String> hash = template.opsForHash();
        List<String> values = hash.values(key);
        if (Objects.isNull(values) || values.isEmpty()) {
            return Collections.emptyList();
        }
        return values.stream()
                .map(it -> new Gson().fromJson(it, DboQiangdan.class))
                // 只筛选出能关联到本平台比赛的强胆
                .filter(it -> !Strings.isNullOrEmpty(it.getMatchNo()))
                .sorted(Comparator.comparing(DboQiangdan::getMatchNo))
                .collect(Collectors.toList());
    }


}
