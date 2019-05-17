package td.news.client.match;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class ClientQiangdanPayIssueNoArrayVo {


    List<Body> data = new ArrayList<>();

    @Data
    public static class Body{

        String IssueNo;
    }
}
