package td.news.biz.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.news.domain.News;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.news.NewsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class HelpService {


    @Autowired private NewsRepository newsRepository;

    /**
     * 帮助列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<News> queryHelpArray(int page, int pageSize) {
        List<DbNews> dboNewses =
                newsRepository.pageHelp(page, pageSize);
        if (Objects.isNull(dboNewses) || dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream()
                .map(News::new)
                .collect(Collectors.toList());
    }
}
