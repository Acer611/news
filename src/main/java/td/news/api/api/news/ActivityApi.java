package td.news.api.api.news;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import td.news.api.api.news.vo.NewsArrayVo;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.news.ActivityService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.common.BizException;
import td.news.repository.news.NewsRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/activity")
@Api(tags = "activity", description = "活动")
public class ActivityApi {

    @Autowired private ActivityService activityService;
    @Autowired private TokenService tokenService;
    @Autowired private NewsRepository newsRepository;

    @GetMapping
    @ApiOperation(
            value = "活动", notes = "最多8个", response = NewsArrayVo.class)
    public ApiResponse newses(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("activity");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        long uid = tokenService.uidWithoutCheck(token);
        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = activityService.listActivity(appCode, channel, cityName, deviceCode, uid, app);
        if (!dboNewses.isEmpty()) {
            dboNewses.stream().forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/pop")
    @ApiOperation(value = "get latest pop activity", response = NewsArrayVo.NewsVo.class)
    public ApiResponse getLatestPopActivity(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {


        long latestTimestamp = newsRepository.getLatestTimestamp("activity_pop");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        long uid = tokenService.uidWithoutCheck(token);
        News latestPopActivity = activityService.getLatestPopActivity(appCode, channel, cityName, deviceCode, uid, app);
        if (Objects.isNull(latestPopActivity) || latestPopActivity.getTimeState() != 2) {
            throw new BizException(17091501, "");
        }
        return new ApiResponse.Ok("", new NewsArrayVo.NewsVo(latestPopActivity), latestTimestamp);
    }
}
