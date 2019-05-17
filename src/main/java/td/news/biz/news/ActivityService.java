package td.news.biz.news;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.device.OnlineDeviceService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
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
public class ActivityService {

    @Autowired private OnlineDeviceService onlineDeviceService;
    @Autowired private NewsRepository newsRepository;


    /**
     * 资讯列表
     *
     * @return
     */
    public List<News> listActivity(int appCode, String channel, String cityName, String deviceCode, long uid, AppType appType) {
        String userTag = !Strings.isNullOrEmpty(deviceCode) ? onlineDeviceService.userTag(deviceCode) : onlineDeviceService.userTag(uid);
        List<DbNews> allActivity = newsRepository.listActivity();
        List<DbNews> dboNewses
                = allActivity.stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .filter(it -> Strings.isNullOrEmpty(userTag) || (Strings.isNullOrEmpty(it.getUserTags()) || it.getUserTags().contains("," + userTag + ",")))
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
                .sorted((o1, o2) -> {
                    int status = 2 == o1.getTimeState() ? 200 : o1.getTimeState();
                    int status2 = 2 == o2.getTimeState() ? 200 : o2.getTimeState();
                    int timeStatusCompare = Integer.compare(status2, status);
                    if (timeStatusCompare != 0) {
                        return timeStatusCompare;
                    }
                    int sortCompare = Integer.compare(o1.getSort(), o2.getSort());
                    if (sortCompare != 0) {
                        return sortCompare;
                    }
                    return o2.getCreate().compareTo(o1.getCreate());
                })
                .collect(Collectors.toList());
    }

    public News getLatestPopActivity(int appCode, String channel, String cityName, String deviceCode, long uid, AppType appType) {
        String userTag = !Strings.isNullOrEmpty(deviceCode) ? onlineDeviceService.userTag(deviceCode) : onlineDeviceService.userTag(uid);
        return newsRepository.listPopActivity().stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .filter(it -> Strings.isNullOrEmpty(userTag) || (Strings.isNullOrEmpty(it.getUserTags()) || it.getUserTags().contains("," + userTag + ",")))
                .map(it -> {
                    it.setContent(null);
                    return new News(it);
                })
                .findFirst().orElse(null);
    }
}
