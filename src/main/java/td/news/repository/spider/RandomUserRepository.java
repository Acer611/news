package td.news.repository.spider;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.JSONMapper;
import td.news.mapper.TD_CP_HD.alt.dbo.T_AltAvatar;
import td.news.repository.spider.dbo.RandomUser;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Service
public class RandomUserRepository {

    public static final String defaultAvatar = "http://common-1253410441.file.myqcloud.com/coverimg/20170621164206.png";

    @Autowired @Qualifier("redis6399db1Template") private StringRedisTemplate db1;
    @Autowired @Qualifier("redis6399db2Template") private StringRedisTemplate db2;

    public RandomUser getRandomUser(long uid, String nickname, String avatar){
        if (!isAlt(uid)) {
            return new RandomUser(nickname, avatar);
        }
        T_AltAvatar alt = randomAvatar(uid);
        if (Objects.nonNull(alt)) {
            return new RandomUser(alt.getNickName(), alt.getAvatar());
        }
        return getRandomUser();
    }

    public RandomUser getRandomUser() {
        SetOperations<String, String> set = db1.opsForSet();
        List<Boolean> booleans = Arrays.asList(true, false);
        String avatar = defaultAvatar;
        Collections.shuffle(booleans);
        if (booleans.get(0)) {
            avatar = set.pop("spider:random:avatar");
        }
        Collections.shuffle(booleans);
        String nick = booleans.get(0) ? generateNick() : set.pop("spider:random:nick");
        nick = !Strings.isNullOrEmpty(nick) ? nick : generateNick();
        return new RandomUser(nick, avatar);
    }

    private static final List<String> mobilePrefix = Arrays.asList(
            (
                    "131,132,133,134,135,136,137,138,139," +
                            "145,147," +
                            "150,151,152,153,155,156,157,158,159," +
                            "173,175,177,176,178," +
                            "180,181,182,183,184,185,186,187,188,189"
            )
                    .split(",")
    );

    public static String generateNick() {
        Collections.shuffle(mobilePrefix);
        String prefix = mobilePrefix.get(0);
        return prefix + "****" + (int) Math.round(Math.random() * (9999 - 1000) + 1000);
    }

    public boolean isAlt(long uid) {
        return db2.hasKey(MessageFormat.format("alt:user_group:{0}", String.valueOf(uid))).booleanValue();
    }

    public T_AltAvatar randomAvatar(long uid) {
        String randomAltGroup = db2.opsForSet().randomMember(MessageFormat.format("alt:user_group:{0}", String.valueOf(uid)));
        if (Strings.isNullOrEmpty(randomAltGroup)) {
            return null;
        }
        String value = db2.opsForSet().randomMember(MessageFormat.format("alt:avatar:{0}", randomAltGroup));
        return !Strings.isNullOrEmpty(value) ? JSONMapper.fromJson(value, T_AltAvatar.class) : null;
    }
}
