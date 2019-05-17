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
import td.news.biz.news.LaunchConfigService;
import td.news.biz.news.domain.News;
import td.news.common.AppType;
import td.news.common.DeviceType;
import td.news.repository.news.NewsRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/config")
@Api(tags = "config", description = "配置相关")
public class LaunchConfigApi {

    @Autowired private NewsRepository newsRepository;
    @Autowired private LaunchConfigService launchConfigService;

    @GetMapping("/launch")
    @ApiOperation(
            value = "启动页", response = NewsArrayVo.class)
    public ApiResponse newses(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @RequestHeader(required = false, value = HK_VERSION) String version,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("launch");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = launchConfigService.latestLaunch(appCode, channel, cityName, deviceType, version,deviceCode, app);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }
}
