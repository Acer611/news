package td.news.repository.spider.dbo;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author sanlion do
 */
public enum AdviceKeyword {

    zhusheng("主胜", Collections.singletonList("主胜")),
    zhufu("主负", Collections.singletonList("主负")),
    rangfu("让负", Collections.singletonList("让负")),
    rangsheng("让胜", Collections.singletonList("让胜")),
    keduibubai("客队不败",Arrays.asList("平","主负")),
    zhuduibubai("主队不败",Arrays.asList("主胜","平"));

    /*
    1.胜 2.平 3.负
    4让胜 5.让平 6.让负
     */

    @Getter private String key;
    @Getter private List<String> result;

    AdviceKeyword(String key, List<String> result) {
        this.key = key;
        this.result = result;
    }

    public static AdviceKeyword byKey(String key){
        try{
            AdviceKeyword[] values = AdviceKeyword.values();
            return Arrays.stream(values).filter(it -> it.getKey().equals(key)).findFirst().get();
        }catch (Exception e){
            return null;
        }
    }
}
