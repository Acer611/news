package td.news.biz.match;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.client.data.DataClient;
import td.news.client.match.ClientMatchArrayVo;
import td.news.client.match.ClientQiangdanPayIssueNoArrayVo;
import td.news.client.match.MasterSchemeClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class MatchService {


    @Autowired private MasterSchemeClient masterSchemeClient;
    @Autowired private DataClient dataClient;




    public List<String> payedQiangdanIssueNo(String token) {
        JsonObject param = new JsonObject();
        param.addProperty("UserCode", token);
        try {
            ClientQiangdanPayIssueNoArrayVo value = masterSchemeClient.qiangdanPayIssueNo(param.toString());
            if (Objects.nonNull(value) && Objects.nonNull(value.getData()) && !value.getData().isEmpty()) {
                return value.getData().stream().map(ClientQiangdanPayIssueNoArrayVo.Body::getIssueNo).collect(Collectors.toList());
            }
        } catch (Exception e) {

        }
        return Collections.emptyList();
    }

    public List<ClientMatchArrayVo.ClientMatchVo> listMultiFootballMatches(Set<String> issueNo) {
        JsonObject params = new JsonObject();
        params.addProperty("Item", issueNo.stream().reduce((a, b) -> a + "," + b).get());
        try {
            return dataClient.listMultiFootballMatches(params.toString()).getData();
        } catch (Exception e) {
            log.error("{}", e);
            return Collections.emptyList();
        }
    }
}
