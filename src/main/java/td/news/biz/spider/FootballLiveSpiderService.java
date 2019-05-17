package td.news.biz.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.repository.spider.MatchAnalyseSpiderRepository;
import td.news.repository.spider.dbo.CacheLiveUrl;

/**
 * @author sanlion do
 */
@Service
public class FootballLiveSpiderService {

    @Autowired private MatchAnalyseSpiderRepository matchAnalyseSpiderRepository;

    public CacheLiveUrl getLiveUrl(String issueNo){
        return matchAnalyseSpiderRepository.liveUrl(issueNo);
    }
}
