package td.news.repository.spider;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.spider.dbo.CacheLiveUrl;
import td.news.repository.spider.dbo.ClientMatchAnalyseDetail;
import td.news.repository.spider.dbo.DboAnalyse;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class MatchAnalyseSpiderRepository {

    @Autowired @Qualifier("redis6399db1Template") private StringRedisTemplate template;


    public void save(List<DboAnalyse> analyses) {
        if (analyses.isEmpty()) {
            return;
        }
        Map<String, String> mValue = new HashMap<>();
        List<String> todayAnalyse = new ArrayList<>();
        analyses.forEach(it -> {
            String json = new Gson().toJson(it);
            todayAnalyse.add(json);
            mValue.put(MessageFormat.format("spider:analyse:{0}", it.getIssueNO()), json);
        });
        template.opsForValue().multiSet(mValue);
        template.delete("spider:analyse:today"); // 先删除老的数据
        template.opsForList().rightPushAll("spider:analyse:today", todayAnalyse);

    }

    public DboAnalyse analyse(String issueNo) {
        String value = template.opsForValue().get(MessageFormat.format("spider:analyse:{0}", issueNo));
        return Strings.isNullOrEmpty(value) ? null : new Gson().fromJson(value, DboAnalyse.class);
    }

    public List<DboAnalyse> analyse() {
        List<String> values = template.opsForList().range("spider:analyse:today", 0, -1);
        return Objects.isNull(values) || values.isEmpty() ? Collections.emptyList()
                : values.stream().filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> new Gson().fromJson(it, DboAnalyse.class)).collect(Collectors.toList());
    }

    public void cleanAnalyse() {
        String pattern = MessageFormat.format(
                "spider:analyse:{0}*",
                LocalDate.now().minusDays(5).format(DateTimeFormatter.ofPattern("yyMMdd")));
        Set<String> keys = template.keys(pattern);
        if (Objects.isNull(keys) || keys.isEmpty()) {
            return;
        }
        keys.forEach(it -> template.delete(it));
    }

    public CacheLiveUrl liveUrl(String issueNo) {
        HashOperations<String, String, String> hash = template.opsForHash();
        String value = hash.get("spider:live", issueNo);
        return !Strings.isNullOrEmpty(value) ? new Gson().fromJson(value, CacheLiveUrl.class) : null;
    }

    public ClientMatchAnalyseDetail getByIssueNo(String issueNo) {
        HashOperations<String, String, String> hash = template.opsForHash();
        String value = hash.get("spider:analyse:detail", issueNo);
        return !Strings.isNullOrEmpty(value) ? new Gson().fromJson(value, ClientMatchAnalyseDetail.class) : null;
    }
}
