package td.news.biz.news;

import com.google.common.base.Strings;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.device.OnlineDeviceService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.news.NewsRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class BannerService {


    @Autowired private NewsRepository newsRepository;
    @Autowired private OnlineDeviceService onlineDeviceService;

    public Map<String, List<News>> listIndexBanner(int appCode, String channel, String cityName, String deviceCode, AppType appType) {
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> originBanners = newsRepository.listIndexBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .filter(it -> Strings.isNullOrEmpty(it.getAppCode()) || it.getAppCode().contains("," + appCode + ","))
                .filter(it -> Strings.isNullOrEmpty(channel) || (Strings.isNullOrEmpty(it.getAppStore()) || it.getAppStore().contains("," + channel + ",")))
                .filter(it -> Strings.isNullOrEmpty(cityName) || (Strings.isNullOrEmpty(it.getCityName()) || it.getCityName().contains("," + cityName + ",")))
                .filter(it -> Strings.isNullOrEmpty(userTag) || (Strings.isNullOrEmpty(it.getUserTags()) || it.getUserTags().contains("," + userTag + ",")))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        Map<String, List<DbNews>> resultMap = new HashMap<>();

       /* resultMap.put("bottomBanners",dboNewses.subList(100,110));
        resultMap.put("middleBanners",dboNewses.subList(110,120));
        resultMap.put("newses",dboNewses.subList(121,130));

        return resultMap;*/

        return dboNewses.stream().map(News::new).collect(Collectors.toList()).stream().collect(Collectors.groupingBy(News::getTypeCode));
    }


    public List<News> listNewsBanner(int appCode, String channel, String cityName, String deviceCode, AppType appType) {
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> originBanners = newsRepository.listNewsBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
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

    public List<News> listDiscoveryBanner(int appCode, String channel, String cityName, String deviceCode, AppType appType) {
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> originBanners = newsRepository.listDiscoveryBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
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

    public List<News> listDiscoveryCategory(int appCode, String channel, String cityName, String deviceCode, AppType appType) {
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> originBanners = newsRepository.listDiscoveryCategory();
        List<DbNews> dboNewses
                = originBanners.stream()
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


    public List<News> listMyBanner(final int appCode, final String channel, final String cityName, final String deviceCode, final AppType appType) {
        String userTag = onlineDeviceService.userTag(deviceCode);
        List<DbNews> originBanners = newsRepository.listMyBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
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

    public List<News> listMasterBanner(AppType appType) {
        List<DbNews> originBanners = newsRepository.listMasterBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                // 若数据库没有配置，则按照AppType.Caipiao处理
                .filter(it -> (Strings.isNullOrEmpty(it.getAppType()) ? ("," + AppType.Caipiao.getCode() + ",") : it.getAppType()).contains("," + appType.getCode() + ","))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }

    public List<News> listBetBanner(String lottery) {
        List<DbNews> originBanners = newsRepository.listBetBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                .filter(it -> Strings.isNullOrEmpty(lottery) || lottery.equalsIgnoreCase(it.getLotteryID()))
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }

    public List<News> listLiveBanner(AppType appType) {
        List<DbNews> originBanners = newsRepository.listLiveBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(td.news.biz.news.domain.News::new).collect(Collectors.toList());
    }

    public List<News> listLiveActivity(AppType appType) {
        List<DbNews> originBanners = newsRepository.listLiveActivity();
        List<DbNews> dboNewses
                = originBanners.stream()
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(td.news.biz.news.domain.News::new).collect(Collectors.toList());
    }

    public List<News> listShopBanner() {
        List<DbNews> originBanners = newsRepository.listShopBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(td.news.biz.news.domain.News::new).collect(Collectors.toList());
    }


    public List<News> listShijiebeiBanner() {
        List<DbNews> originBanners = newsRepository.listShijiebeiBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }

    public List<News> listTuidanBanner() {
        List<DbNews> originBanners = newsRepository.listTuidanBanner();
        List<DbNews> dboNewses
                = originBanners.stream()
                .peek(it -> it.setContent(null))
                .collect(Collectors.toList());
        if (dboNewses.isEmpty()) {
            return Collections.emptyList();
        }
        return dboNewses.stream().map(News::new).collect(Collectors.toList());
    }





}
