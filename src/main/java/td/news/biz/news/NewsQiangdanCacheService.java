package td.news.biz.news;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import td.news.biz.match.MatchService;
import td.news.biz.match.domain.Match;
import td.news.biz.match.domain.MatchResult;
import td.news.biz.news.domain.News;
import td.news.biz.news.domain.NewsQiangdan;
import td.news.common.IssueHelper;
import td.news.common.MatchRateHelper;
import td.news.repository.spider.NewsSpiderRepository;
import td.news.repository.spider.dbo.DboQiangdan;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static td.news.configuration.CacheConfiguration.bizCache_60_min;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class NewsQiangdanCacheService {

    @Autowired private NewsSpiderRepository newsSpiderRepository;
    @Autowired private NewsService newsService;
    @Autowired private MatchService matchService;

    @Cacheable(value = bizCache_60_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<NewsQiangdan> list(String issue) {
        List<DboQiangdan> value
                = newsSpiderRepository.listByDay(IssueHelper.getDayOfYearByIssue(issue));
        if (Objects.isNull(value) || value.isEmpty()) {
            return Collections.emptyList();
        }

        // 赛果处理
        Map<String, Match> result =
                matchService.listMultiFootballMatches(value.stream().map(it ->it.getIssueNo()).collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(it -> it.getIssueNo(), it-> new Match(it)));


        List<String> allNewsLink = Lists.newArrayList();
        value.forEach(it -> allNewsLink.addAll(it.getRef()));
        List<News> news = newsService.listByLinkUrl(allNewsLink);
        Map<String, List<News>> newsMap = news.stream().map(it -> {
            it.setContent(null);
            return it;
        }).collect(Collectors.groupingBy(News::getLink));
        List<NewsQiangdan> qiangdanArray = value.stream()
                .map(NewsQiangdan::new)
                .map(it -> {
                    if (!it.getRef().isEmpty()) {
                        it.getRef().forEach(iit -> {
                            if (newsMap.containsKey(iit)) {
                                it.getNewses().add(newsMap.get(iit).get(0));
                            }
                        });
                    }
                    if (result.containsKey(it.getIssueNo())) {
                        Match match = result.get(it.getIssueNo());
                        it.getSpfRate().addAll(MatchRateHelper.rate(match.getSpspf()));
                        it.getRqspfRate().addAll(MatchRateHelper.rate(match.getRqSpf()));
                        if (match.getState() == 2) {
                            it.setMatchResult(
                                    new MatchResult(
                                            it.isRq() ? match.getRqResult() : match.getResult(),
                                            match.getHomeScore(),
                                            match.getVisitorScore()));
                        }
                    }
                    return it;
                })
                .collect(Collectors.toList());
        return qiangdanArray;
    }
}
