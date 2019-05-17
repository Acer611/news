package td.news.repository.news;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import td.news.common.JSONMapper;
import td.news.mapper.TD_CP_News.AnnouncementMapper;
import td.news.mapper.TD_CP_News.BannerMapper;
import td.news.mapper.TD_CP_News.NewsMapper;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.DboNewsCategory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static td.news.configuration.CacheConfiguration.bizCache_10_min;
import static td.news.configuration.CacheConfiguration.bizCache_1_min;
import static td.news.configuration.CacheConfiguration.bizCache_30_min;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class NewsRepository {

    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private AnnouncementMapper announcementMapper;
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    @Qualifier("redis6399db7Template")
    private StringRedisTemplate db7;


    //    @Cacheable(announcementCache)
    @Cacheable(value = bizCache_1_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> allAvailable() {
        return announcementMapper.allAvailable();
    }

    //    @Cacheable(latestOneMonthNews)
    @Cacheable(value = bizCache_30_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> queryLatestOneMonth(String code) {
        return newsMapper.queryLatestOneMonth(code);
    }


    //    @Cacheable(imAnnoCache)
    @Cacheable(value = bizCache_1_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> listIMAnno() {
        return announcementMapper.all_for_im();
    }

    //    @Cacheable(masterIMAnnoCache)
    @Cacheable(value = bizCache_1_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> listMasterIMAnno() {
        return announcementMapper.all_for_mater_im();
    }

    @Cacheable(value = bizCache_1_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> listAnnoByShijiebeiZhuli() {
        return announcementMapper.listAnnoByShijiebeiZhuli();
    }

    @Cacheable(value = bizCache_10_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> listByCode(String code) {
        return announcementMapper.listByCode(code);
    }

    //    @Cacheable(cpxtCache)
    @Cacheable(value = bizCache_30_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbNews> listCpxt() {
        return newsMapper.listCpxt();
    }

    /**
     * 阅读量统计，每半小时统计入Db
     *
     * @param id
     */
    public void countRpt(long id) {
        db7.opsForHash().increment("counts:news_read", String.valueOf(id), 1);
    }

    public DbNews getNews(long id) {
        DbNews dbNews = JSONMapper.fromJson(db7.opsForValue().get(MessageFormat.format("news:detail:{0}", String.valueOf(id))), DbNews.class);
        if (Objects.nonNull(dbNews)) {
            return dbNews;
        }
        dbNews = newsMapper.byId(id);
        if (Objects.nonNull(dbNews)) {
            db7.opsForValue().set(
                    MessageFormat.format("news:detail:{0}", String.valueOf(id)),
                    JSONMapper.toJson(dbNews),
                    7, TimeUnit.DAYS);
        }
        return dbNews;
    }

    public List<DbNews> listIndexBanner() {

        List<DbNews> news =  bannerMapper.listAllBanner();
        return news;
       /* return db7.opsForList().range("news:banner_index", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());*/
    }

    public List<DbNews> listNewsBanner() {
        return db7.opsForList().range("news:banner_news", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listDiscoveryBanner() {
        return db7.opsForList().range("news:banner_discovery", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listMasterBanner() {
        return db7.opsForList().range("news:banner_di", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listLiveBanner() {
        return db7.opsForList().range("news:banner_live", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<DbNews> listLiveActivity() {
        return db7.opsForList().range("news:banner_live_activity", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<DbNews> listShopBanner() {
        return db7.opsForList().range("news:banner_shop", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<DbNews> listBetBanner() {
        return db7.opsForList().range("news:banner_bet", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listDiscoveryCategory() {
        return db7.opsForList().range("news:banner_discovery_category", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listShijiebeiBanner() {
        return db7.opsForList().range("news:banner_shijiebei", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listTuidanBanner() {
        return db7.opsForList().range("news:banner_tuidan", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listMyBanner() {
        return db7.opsForList().range("news:banner_my", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listActivity() {
        return db7.opsForList().range("news:activity", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listPopActivity() {
        return db7.opsForList().range("news:activity_pop", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> pageHelp(int page, int pageSize) {
        return db7.opsForList().range("news:help", (page - 1) * pageSize, page * pageSize - 1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listLaunch() {
        return db7.opsForList().range("news:launch", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listHot() {
        return db7.opsForList().range("news:hot", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DbNews> listTodayHot() {
        return db7.opsForList().range("news:hot_today", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public List<DboNewsCategory> listNewsCategory() {
        return db7.opsForList().range("news:category", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DboNewsCategory.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }

    public long getLatestTimestamp(String key) {
        HashOperations<String, String, String> hash = db7.opsForHash();
        String value = hash.get("news:stamp", key);
        return !Strings.isNullOrEmpty(value) ? Long.parseLong(value) : 0;
    }

    public long getAnnoStamp() {
        String stamp_key = "news:stamp:anno";
        String value = db7.opsForValue().get(stamp_key);
        if (!Strings.isNullOrEmpty(value)) {
            return Long.parseLong(value);
        }
        long stamp = System.currentTimeMillis();
        db7.opsForValue().set(stamp_key, String.valueOf(stamp), 5, TimeUnit.MINUTES);
        return stamp;
    }

    public long getTimestamp(String bizPrefix, long expire, TimeUnit timeUnit) {
        String stamp_key = "stamp:" + bizPrefix;
        String value = db7.opsForValue().get(stamp_key);
        if (!Strings.isNullOrEmpty(value)) {
            return Long.parseLong(value);
        }
        long stamp = System.currentTimeMillis();
        db7.opsForValue().set(stamp_key, String.valueOf(stamp), expire, timeUnit);
        return stamp;
    }

    public List<DbNews> newsFive() {
        return db7.opsForList().range("news:qingbaozhan", 0, -1).stream()
                .filter(it -> !Strings.isNullOrEmpty(it))
                .map(it -> JSONMapper.fromJson(it, DbNews.class))
                .filter(it -> Objects.nonNull(it))
                .collect(Collectors.toList());
    }
}
