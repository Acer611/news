package td.news.repository.spider.dbo;

import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author sanlion do
 */
@Data
@AllArgsConstructor
public class LoadedNewsItem {

    private String link;
    private String title;
    private Date date;
    private NewsSource source;
    private String key;

    public static String md5(String link) {
        return Hashing.md5().newHasher()
                .putString(link, Charset.defaultCharset())
                .hash().toString();
    }

}
