package td.news.api.api.banner;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.banner.vo.IndexBannerArrayVo;
import td.news.api.api.banner.vo.NewsBannerArrayVo;
import td.news.api.api.news.vo.NewsArrayVo;
import td.news.api.api.news.vo.ObjApiResponse;
import td.news.api.common.response.ApiResponse;
import td.news.biz.news.BannerService;
import td.news.biz.news.NewsType;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.common.DeviceType;
import td.news.mapper.TD_CP_News.dbo.DbNews;
import td.news.repository.news.NewsRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static td.news.biz.news.NewsType.banner_index_bottom;
import static td.news.biz.news.NewsType.banner_index_middle;
import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/banner")
@Api(tags = "banner", description = "轮播图")
public class BannerApi {

    @Autowired private BannerService bannerService;
    @Autowired private NewsRepository newsRepository;

    @GetMapping
    @ApiOperation(
            value = "轮播图列表", response = IndexBannerArrayVo.class)
    public ApiResponse listBanners(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {

        IndexBannerArrayVo vo = new IndexBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_index");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("首页轮播图，时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";

        Map<String, List<News>> banners = bannerService.listIndexBanner(appCode, channel, cityName, deviceCode, app);
        System.out.println(banners.size());
     /*   // 顶部
        banners.forEach((code, news) -> {
            if (NewsType.banner_index.getCode().equals(code)) {
                news.forEach(it -> vo.getNewses().add(new NewsArrayVo.NewsVo(it)));
            }
            if (banner_index_middle.getCode().equals(code)) {
                news.forEach(it -> vo.getMiddleBanners().add(new NewsArrayVo.NewsVo(it)));
            }
            if (banner_index_bottom.getCode().equals(code)) {
                news.forEach(it -> vo.getBottomBanners().add(new NewsArrayVo.NewsVo(it)));
            }
        });*/

        List<News> newses = new ArrayList<>();
        List<News> middleBanners = new ArrayList<>();
        List<News> bottomBanners = new ArrayList<>();
        for(String key:banners.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
            List<News> newsInfoList = banners.get(key);//
            for (News newinfo: newsInfoList) {
                if(NewsType.banner_index.getCode().equalsIgnoreCase(newinfo.getCode())){
                    newses.add(newinfo);
                } else if(NewsType.banner_index_middle.getCode().equalsIgnoreCase(newinfo.getCode())){
                    middleBanners.add(newinfo);

                } else if (NewsType.banner_index_bottom.getCode().equalsIgnoreCase(newinfo.getCode())){
                    bottomBanners.add(newinfo);
                }
            }
        }

        Map resultMap = new HashMap();
        resultMap.put("newses",newses);
        resultMap.put("middleBanners",middleBanners);
        resultMap.put("bottomBanners",bottomBanners);

        return new ApiResponse.Ok("",resultMap , latestTimestamp);
    }


    @GetMapping("/4news")
    @ApiOperation(
            value = "资讯首页Banner列表", response = NewsBannerArrayVo.class)
    public ApiResponse listNewsBanner(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_news");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = bannerService.listNewsBanner(appCode, channel, cityName, deviceCode, app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ObjApiResponse("", vo, latestTimestamp);
    }

    @GetMapping("/4di")
    @ApiOperation(value = "彩帝首页BANNER", response = NewsBannerArrayVo.class)
    public ApiResponse listDiBanner(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp
    ) {
        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_di");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> dboNewses = bannerService.listMasterBanner(app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/byBet")
    @ApiOperation(value = "彩种投注页Banner", response = NewsArrayVo.NewsVo.class)
    public ApiResponse listBannerByBet(
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @ApiParam("彩种ID") @RequestParam String lottery,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {


        long latestTimestamp = DeviceType.Android.equals(deviceType) ? newsRepository.getLatestTimestamp("banner_bet") : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        List<News> dboNewses = bannerService.listBetBanner(lottery);
        if (!dboNewses.isEmpty()) {
            News latest = dboNewses.stream().findAny().orElse(null);
            return new ApiResponse.Ok("", new NewsArrayVo.NewsVo(latest), latestTimestamp);
        }
        return new ApiResponse.Fail(true, 17112706, "暂无");
    }

    @GetMapping("/byDiscovery")
    @ApiOperation(value = "发现首页轮播图", response = NewsBannerArrayVo.class)
    public ApiResponse listBannerByDiscovery(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_discovery");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = bannerService.listDiscoveryBanner(appCode, channel, cityName, deviceCode, app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/listDiscoveryCategory")
    @ApiOperation(value = "发现首页频道", response = NewsBannerArrayVo.class)
    public ApiResponse listDiscoveryCategory(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_discovery_category");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = bannerService.listDiscoveryCategory(appCode, channel, cityName, deviceCode, app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/listByUCenter")
    @ApiOperation(value = "个人中心配置", response = NewsBannerArrayVo.class)
    public ApiResponse listByMy(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_my");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = bannerService.listMyBanner(appCode, channel, cityName, deviceCode, app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/byShijiebei")
    @ApiOperation(value = "世界杯专题轮播图", response = NewsBannerArrayVo.class)
    public ApiResponse listBannerByShijiebei(
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {


        long latestTimestamp = newsRepository.getLatestTimestamp("banner_shijiebei");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        NewsBannerArrayVo newsBannerArrayVo = new NewsBannerArrayVo();
        List<News> dboNewses = bannerService.listShijiebeiBanner();
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(it -> newsBannerArrayVo.getNewses().add(new NewsArrayVo.NewsVo(it)));
        }
        return new ApiResponse.Ok("", newsBannerArrayVo, stamp);
    }

    @GetMapping("/byTuidan")
    @ApiOperation(value = "彩帝吧推单列表广告位", response = NewsBannerArrayVo.class)
    public ApiResponse listBannerByTuian(
        @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {
            long latestTimestamp = newsRepository.getLatestTimestamp("banner_tuidan");
            if (latestTimestamp != 0 && latestTimestamp == stamp) {
                log.debug("时间戳命中");
                return new ApiResponse.Ok(latestTimestamp);
            }

            NewsBannerArrayVo newsBannerArrayVo = new NewsBannerArrayVo();
            List<News> dboNewses = bannerService.listTuidanBanner();
            if (!dboNewses.isEmpty()) {
                dboNewses.forEach(it -> newsBannerArrayVo.getNewses().add(new NewsArrayVo.NewsVo(it)));
            }
            return new ApiResponse.Ok("", newsBannerArrayVo, stamp);
    }



    @GetMapping("/4live")
    @ApiOperation(value = "直播首页轮播图", response = NewsArrayVo.NewsVo.class)
    public ApiResponse listBannerByLive(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_live");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> dboNewses = bannerService.listLiveBanner(app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/4liveActivity")
    @ApiOperation(value = "直播间活动", response = NewsArrayVo.NewsVo.class)
    public ApiResponse listBannerByLiveActivity(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_live_activity");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> dboNewses = bannerService.listLiveActivity(app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/byShop")
    @ApiOperation(value = "彩店联盟首页轮播图", response = NewsArrayVo.NewsVo.class)
    public ApiResponse byShop(
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        NewsBannerArrayVo vo = new NewsBannerArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("banner_shop");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> dboNewses = bannerService.listShopBanner();
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }
}
