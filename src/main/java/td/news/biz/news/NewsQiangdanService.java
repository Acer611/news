package td.news.biz.news;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.match.MatchService;
import td.news.biz.news.domain.NewsQiangdan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class NewsQiangdanService {

    @Autowired private MatchService matchService;
    @Autowired private  NewsQiangdanCacheService newsQiangdanCacheService;

    public List<NewsQiangdan> list(String issue, String token) {
        List<NewsQiangdan> qiangdanArray = newsQiangdanCacheService.list(issue);
        // 加载付费查看权限
        if (!Strings.isNullOrEmpty(token)) {
            List<String> rights = rightIssueNo(token);
            if (!rights.isEmpty()) {
                qiangdanArray.forEach(it -> it.setRight(rights.contains(it.getIssueNo())));
            }
        }
        return qiangdanArray;
    }

    public List<String> listLatestIssueArray() {
        List<String> value = Lists.newArrayList();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            value.add(today.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return value;
    }

    private List<String> rightIssueNo(String token) {
        return matchService.payedQiangdanIssueNo(token);
    }
}
