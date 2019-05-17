package td.news.biz.news;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.bet.BetOpenService;
import td.news.biz.device.OnlineDeviceService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.common.DeviceType;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.news.NewsRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class LaunchConfigService {

    @Autowired private BetOpenService betOpenService;
    @Autowired private OnlineDeviceService onlineDeviceService;
    @Autowired private NewsRepository newsRepository;

    /**
     * 轮播图列表
     *
     * @return
     */
    public List<News> latestLaunch(
            int appCode, String channel, String cityName, DeviceType deviceType, String version, String deviceCode, AppType appType) {
        boolean ban = betOpenService.isBan(deviceType, deviceCode, appCode, version, cityName, channel);
        if (ban) {
            return Collections.emptyList();
        }
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> allAvailableLaunchConfig = newsRepository.listLaunch();
        List<DbNews> dboNewses
                = allAvailableLaunchConfig.stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .filter(it -> Strings.isNullOrEmpty(userTag) || (Strings.isNullOrEmpty(it.getUserTags()) || it.getUserTags().contains("," + userTag + ",")))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }
}
