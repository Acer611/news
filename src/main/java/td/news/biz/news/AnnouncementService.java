package td.news.biz.news;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.device.OnlineDeviceService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.mapper.TD_CP_News.BannerMapper;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.message.BetOpenRepository;
import td.news.repository.message.dbo.DboBetOpen;
import td.news.repository.news.NewsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class AnnouncementService {


    @Autowired private OnlineDeviceService onlineDeviceService;
    @Autowired private BetOpenRepository betOpenRepository;
    @Autowired private NewsRepository newsRepository;
    @Autowired private BannerMapper bannerMapper;

    /**
     * 公告
     *
     * @return
     */
    public List<News> queryAnnouncementArray(int appCode, String channel, String cityName, String deviceCode, AppType appType) {
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> dboNewses
                = newsRepository.allAvailable().stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("%," + channel + ",%")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("%," + cityName + ",%")))
                .filter(it -> Strings.isNullOrEmpty(userTag) || (Strings.isNullOrEmpty(it.getUserTags()) || it.getUserTags().contains("%," + userTag + ",%")))
                .map(it -> {
                    it.setContent(null);
                    return it;
                })
                .collect(Collectors.toList());
        if (Objects.isNull(dboNewses) || dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream()
                .map(News::new)
                .collect(Collectors.toList());
    }

    /**
     * 中奖公告
     *
     * @param lottery
     * @return
     */
    public List<News> listBetOpenByLottery(int lottery, int appCode) {

        // 否则取中奖信息
        List<DboBetOpen> dboBetOpens = betOpenRepository.listBetOpenByLottery(lottery, appCode);
        return dboBetOpens.isEmpty()
                ? Collections.emptyList()
                : dboBetOpens.stream().map(it -> new News(it, lottery)).collect(Collectors.toList());
    }

    public List<News> listAll(int lottery, AppType appType,
                              int appCode, String channel, String cityName, String deviceCode) {
        if (lottery == 0) {
            // 优先取公告
            List<News> announcement = queryAnnouncementArray(appCode, channel, cityName, deviceCode, appType);
            if (!announcement.isEmpty()) {
                return announcement;
            }
        }
        return listBetOpenByLottery(lottery, appCode);
    }

    public List<News> listIMAnno() {
        return newsRepository.listIMAnno()
                .stream()
                .filter(it -> Objects.nonNull(it))
                .map(News::new)
                .collect(Collectors.toList());
    }

    public List<News> listMasterIMAnno() {
        return newsRepository.listMasterIMAnno()
                .stream()
                .filter(it -> Objects.nonNull(it))
                .map(News::new)
                .collect(Collectors.toList());
    }

    public List<News> listAnnoByShijiebeiZhuli() {
        return newsRepository.listAnnoByShijiebeiZhuli()
                .stream()
                .filter(it -> Objects.nonNull(it))
                .map(News::new)
                .collect(Collectors.toList());
    }

    public List<News> listByCode(String code, AppType appType) {
        return newsRepository.listByCode(code)
                .stream()
                .filter(it -> Objects.nonNull(it))
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .map(News::new)
                .collect(Collectors.toList());
    }

    public List<News> findAllAnnouncement(int lottery, AppType app, int appCode, String channel, String cityName, String deviceCode) {
        List<DbNews> dbNews= bannerMapper.findAllAnnouncement("1002");

        return dbNews.stream().map(News::new).collect(Collectors.toList());

    }
}
