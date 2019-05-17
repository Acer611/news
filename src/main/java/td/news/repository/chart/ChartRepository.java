package td.news.repository.chart;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.chart.cache.CacheChartUrl;

/**
 * @author sanlion do
 */
@Service
public class ChartRepository {

    @Autowired
    @Qualifier("redis6399db1Template")
    private StringRedisTemplate db1;

    public CacheChartUrl getChartRedirectByLottery(int lottery){
        HashOperations<String, String, String> hash = db1.opsForHash();
        String value = hash.get("spider:chart.new", String.valueOf(lottery));
        return Strings.isNullOrEmpty(value)? null : new Gson().fromJson(value, CacheChartUrl.class);
    }

}
