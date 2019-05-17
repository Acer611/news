package td.news.repository.di;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.repository.di.dbo.T_MasterRegionConfig;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class MasterRegionConfigRepository {

    @Autowired
    @Qualifier("redis6399db2Template")
    private StringRedisTemplate db2;


    public List<Long> listRegionMaster(long regionID) {
        if (regionID == 0) {
            return Collections.emptyList();
        }
        return db2.opsForSet().members("di:region").stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> new Gson().fromJson(it, T_MasterRegionConfig.class))
                .filter(it -> Objects.nonNull(it))
                .filter(it -> regionID == it.getRegionID())
                .map(it -> it.getMasterID())
                .collect(Collectors.toList());
    }
}
