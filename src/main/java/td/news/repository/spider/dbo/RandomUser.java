package td.news.repository.spider.dbo;

import com.google.common.hash.Hashing;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * @author sanlion do
 */
@Data
public class RandomUser {

    String id;
    String nick;
    String avatar;

    public RandomUser(String nick, String avatar) {
        this.nick = nick;
        this.avatar = avatar;
        this.id = Hashing.md5().newHasher().putString(nick, Charset.defaultCharset())
                .putString(avatar, Charset.defaultCharset()).hash().toString();
    }
}
