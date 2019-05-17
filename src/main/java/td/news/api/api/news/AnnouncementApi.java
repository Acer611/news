package td.news.api.api.news;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.news.vo.NewsArrayVo;
import td.news.api.common.response.ApiResponse;
import td.news.biz.news.AnnouncementService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.common.DeviceType;
import td.news.repository.news.NewsRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/announcement")
@Api(tags = "announcement", description = "公告")
public class AnnouncementApi {

    @Autowired private NewsRepository newsRepository;
    @Autowired private AnnouncementService announcementService;

    @GetMapping
    @ApiOperation(
            value = "公告", response = NewsArrayVo.class)
    public ApiResponse newses(
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getAnnoStamp();
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> indexBetOpen = announcementService.listBetOpenByLottery(0, appCode);
        indexBetOpen
                .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/bet")
    @ApiOperation(
            value = "投注页开奖公告", response = NewsArrayVo.class)
    public ApiResponse listOpenBet(
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @ApiParam("彩种ID（首页=0）") @RequestParam(defaultValue = "0") int lottery,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = DeviceType.Android.equals(deviceType) ? newsRepository.getAnnoStamp() : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("彩种公告时间戳命中/bet");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> indexBetOpen = announcementService.listBetOpenByLottery(lottery, appCode);
        indexBetOpen
                .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/all")
    @ApiOperation(
            value = "大而全的公告：兼容普通公告以及中奖通知", response = NewsArrayVo.class)
    public ApiResponse all(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @ApiParam("彩种ID（首页=0）") @RequestParam(defaultValue = "0") int lottery,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = DeviceType.Android.equals(deviceType) ? newsRepository.getAnnoStamp() : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("公告时间戳命中/all");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }


        List<News> all = announcementService.listAll(lottery, app, appCode, channel, cityName, deviceCode);
        all.stream()
                .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));

        //TODO 从数据库中查询公告
        List<News> news = announcementService.findAllAnnouncement(lottery, app, appCode, channel, cityName, deviceCode);
        System.out.println(news.size());
        Map resultMap = new HashMap();
        resultMap.put("newses",news);
        return new ApiResponse.Ok("", resultMap, latestTimestamp);
    }

    @GetMapping("/im")
    @ApiOperation(
            value = "聊天室公告", response = NewsArrayVo.class)
    public ApiResponse im() {
        NewsArrayVo vo = new NewsArrayVo();
        List<News> all = announcementService.listIMAnno();
        all.stream()
                .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        return new ApiResponse.Ok("", vo);
    }

    @GetMapping("/byMasterIM")
    @ApiOperation(
            value = "聊天室公告<彩帝", response = NewsArrayVo.class)
    public ApiResponse byMasterIM() {
        NewsArrayVo vo = new NewsArrayVo();
        List<News> all = announcementService.listMasterIMAnno();
        all.stream()
                .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        return new ApiResponse.Ok("", vo);
    }

    @GetMapping("/byShijiebeiZl")
    @ApiOperation(
            value = "世界杯助力活动公告", response = NewsArrayVo.class)
    public ApiResponse byShijiebeiZl() {
        NewsArrayVo vo = new NewsArrayVo();
//        List<News> all = announcementService.listAnnoByShijiebeiZhuli();
//        all.stream()
//                .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        return new ApiResponse.Ok("", vo);
    }

    @GetMapping("/byCode")
    @ApiOperation(
            value = "通过类型code获取公告", response = NewsArrayVo.class)
    public ApiResponse byCode(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestParam String code,
            @RequestParam(required = false, defaultValue = "1") int limit) {
        NewsArrayVo vo = new NewsArrayVo();
        List<News> all = announcementService.listByCode(code, app);
        all.stream()
                .limit(limit).forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        return new ApiResponse.Ok("", vo);
    }
}
