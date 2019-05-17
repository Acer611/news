package td.news.biz.news;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.news.domain.News;
import td.news.biz.news.domain.NewsCategory;
import td.news.common.AppType;
import td.news.mapper.TD_CP_News.NewsMapper;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.DboNewsCategory;
import td.news.repository.news.NewsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class NewsService {


    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private NewsRepository newsRepository;

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public News byId(long id) {
        DbNews newsInformation = newsRepository.getNews(id);
        if (Objects.isNull(newsInformation)) {
            return null;
        }
        return new News(newsInformation);
    }

    public List<News> queryNewses(String code, int appCode, String channel, String cityName, int page, int pageSize) {

        if ("10052".equalsIgnoreCase(code)) {
            return listCpxt(page, pageSize);
        }

        List<DbNews> latestOneMonthNews = newsRepository.queryLatestOneMonth(code);
        List<DbNews> dboNewses
                = latestOneMonthNews.stream()
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                // 之前为了区分不同appcode取出来的列表不一致，特地做了id计算
                // 现估计那个业务不需要了，暂时注释掉
//                .filter(it -> (it.getID() / appCode) % 2 == 0)
                .skip((page - 1) * pageSize).limit(pageSize)
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }

    private List<News> listCpxt(int page, int pageSize) {
        List<DbNews> latestOneMonthNews = newsRepository.listCpxt();
        List<DbNews> dboNewses
                = latestOneMonthNews.stream()
                .skip((page - 1) * pageSize).limit(pageSize)
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }

    public List<News> queryHotNewses(int appCode, String channel, String cityName, int page, int pageSize) {
        return queryHotNewses(appCode, channel, cityName).stream().skip((page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    private List<News> queryHotNewses(int appCode, String channel, String cityName) {
        List<DbNews> dboNewses = newsRepository.listHot().stream()
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }


    public List<News> queryTodayHot(int appCode, String channel, String cityName, int page, int pageSize) {
        List<DbNews> dboNewses = newsRepository.listTodayHot().stream()
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        return dboNewses.stream().map(News::new).skip((page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public List<NewsCategory> listCategory(int appCode, AppType appType) {

        List<DboNewsCategory> values = newsRepository.listNewsCategory();
        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        boolean anyMatch = values.stream()
                .map(DboNewsCategory::getAppCode)
                .filter(it -> !Strings.isNullOrEmpty(it))
                .anyMatch(it -> it.contains("," + appCode + ","));

        List<DboNewsCategory> dboNewsCategories
                = values.stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (!anyMatch || !Strings.isNullOrEmpty(it.getAppCode()) && it.getAppCode().contains("," + appCode + ",")))
                .collect(Collectors.toList());
        return dboNewsCategories.stream().map(NewsCategory::new).collect(Collectors.toList());
    }

    public List<NewsCategory> listV2Category() {

        List<DboNewsCategory> values = newsMapper.listV2Category();
        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        return values.stream().map(NewsCategory::new).collect(Collectors.toList());
    }

    List<News> listByLinkUrl(List<String> links) {
        if (Objects.isNull(links) || links.isEmpty()) {
//            log.error("fuck, how can u give me an empty list.");
            return Collections.emptyList();
        }
        List<DbNews> value = newsMapper.queryNewsBySpecifyUrl(links);
        if (Objects.isNull(value) || value.isEmpty()) {
            return Collections.emptyList();
        }
        return value.stream().map(News::new).collect(Collectors.toList());
    }

    public void countRpt(long newsid) {
//        newsMapper.countRpt(newsid);
        newsRepository.countRpt(newsid);
    }

    public List<News> newsFive(int appCode, String channel, String cityName) {
        List<DbNews> dboNewses = newsRepository.newsFive().stream()
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }
}
