package td.news.biz.spider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.repository.spider.MatchAnalyseSpiderRepository;
import td.news.repository.spider.dbo.DboAnalyse;

import java.util.List;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class MatchAnalyseSpiderService {

    @Autowired private MatchAnalyseSpiderRepository matchAnalyseSpiderRepository;

    public DboAnalyse analyse(String issueNo) {
        return matchAnalyseSpiderRepository.analyse(issueNo);
    }

    public List<DboAnalyse> analyse() {
        return matchAnalyseSpiderRepository.analyse();
    }




}
