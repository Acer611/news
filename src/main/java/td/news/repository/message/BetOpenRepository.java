package td.news.repository.message;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.message.dbo.DboBetOpen;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class BetOpenRepository {


    @Autowired @Qualifier("redis6399Template") private StringRedisTemplate db0;
    @Autowired @Qualifier("redis6399db6Template") private StringRedisTemplate db6;

    public List<DboBetOpen> listBetOpenByLottery(int lottery, int appCode) {

        if (170 != appCode) {
            return Collections.emptyList();
        }

        String key = MessageFormat.format("HD_Activity_8:{0}", String.valueOf(lottery));
        String value = db6.opsForValue().get(key);

        return Strings.isNullOrEmpty(value) ? Collections.emptyList() :
                new Gson().fromJson(
                        value, new TypeToken<ArrayList<DboBetOpen>>() {
                        }.getType());
    }
}
