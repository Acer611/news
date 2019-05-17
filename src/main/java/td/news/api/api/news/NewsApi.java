package td.news.api.api.news;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.banner.vo.NewsBannerArrayVo;
import td.news.api.api.news.vo.*;
import td.news.api.common.response.ApiResponse;
import td.news.biz.device.AppCodeService;
import td.news.biz.news.NewsQiangdanService;
import td.news.biz.news.NewsService;
import td.news.biz.news.domain.News;
import td.news.biz.news.domain.NewsCategory;
import td.news.biz.news.domain.NewsQiangdan;
import td.news.common.AppType;
import td.news.common.BizException;
import td.news.common.DeviceType;
import td.news.mapper.TD_CP_News.NewsMapper;
import td.news.repository.news.NewsRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/news")
@Api(tags = "news", description = "资讯")
public class NewsApi {

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsQiangdanService newsQiangdanService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private AppCodeService appCodeService;

    @GetMapping("/{id}")
    @ApiOperation(value = "资讯详情", response = NewsArrayVo.NewsVo.class)
    public ApiResponse byId(@PathVariable long id,
                            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode) {
        News news = newsService.byId(id);
        if (Objects.isNull(news)) {
            throw new BizException(17060701, "资讯不存在");
        }
        NewsArrayVo.NewsVo value
                = new NewsArrayVo.NewsVo(news);
        if (id == 45 || id == 46) {
            String newContent
                    = MessageFormat.format(value.getContent(), "本应用");
            value.setContent(newContent);
        }
        newsService.countRpt(id);
        return new ApiResponse.Ok("", value);
    }

    @GetMapping("/hot")
    @ApiOperation(value = "热门资讯列表", response = NewsArrayVo.class)
    public ApiResponse listHotNews(
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("hot");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = newsService.queryHotNewses(appCode, channel, cityName, page, pageSize);
        if (!dboNewses.isEmpty()) {
            dboNewses.stream().forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/todayHot")
    @ApiOperation(value = "今日热门资讯列表", response = NewsArrayVo.class)
    public ApiResponse listTodayHotNews(
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        // 产品要求：今日头条取全部头条
        // 产品第二次需求变更：今日头条只取后台配置的今日的
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("hot_today");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = newsService.queryTodayHot(appCode, channel, cityName, page, pageSize);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }

    @GetMapping("/category")
    @ApiOperation(value = "获取资讯分类", response = NewsCategoryVo.ArrayVo.class)
    public ApiResponse listCategory(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "0") int appCode,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {
        NewsCategoryVo.ArrayVo arrayVo = new NewsCategoryVo.ArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("category");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", arrayVo, latestTimestamp);
        }

        List<NewsCategory> newsCategories = newsService.listCategory(appCode, app);
        if (!newsCategories.isEmpty()) {
            newsCategories.forEach(it -> arrayVo.getCategories().add(new NewsCategoryVo(it)));
        }
        return new ApiResponse.Ok("", arrayVo, latestTimestamp);
    }

    @GetMapping("/category/v2")
    @ApiOperation(value = "获取资讯分类", response = NewsCategoryVo.ArrayVo.class)
    public ApiResponse listCategory_v2(
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {
        NewsCategoryVo.ArrayVo arrayVo = new NewsCategoryVo.ArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("category.v2");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", arrayVo, latestTimestamp);
        }

        List<NewsCategory> newsCategories = newsService.listV2Category();
        if (!newsCategories.isEmpty()) {
            newsCategories.forEach(it -> arrayVo.getCategories().add(new NewsCategoryVo(it)));
        }
        return new ApiResponse.Ok("", arrayVo, latestTimestamp);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询资讯列表<=子目录", response = NewsArrayVo.class)
    public ApiResponse listNews(
            @ApiParam("编码") @RequestParam String code,
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize) throws UnsupportedEncodingException {
        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        NewsArrayVo vo = new NewsArrayVo();
        List<News> dboNewses = newsService.queryNewses(code, appCode, channel, cityName, page, pageSize);
        if (!dboNewses.isEmpty()) {
            dboNewses.stream().forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ObjApiResponse("", vo);
    }

    @GetMapping("/qd/issue/:all")
    @ApiOperation(value = "强胆、博胆期次列表", response = QiangdanIssueVo.QiangdanIssueArrayVo.class)
    public ApiResponse listAllIssueName() {
        QiangdanIssueVo.QiangdanIssueArrayVo arrayVo = new QiangdanIssueVo.QiangdanIssueArrayVo();
        List<String> value = newsQiangdanService.listLatestIssueArray();
        if (Objects.nonNull(value) && !value.isEmpty()) {
            value.forEach(it -> arrayVo.getValue().add(it));
        }
        return new ApiResponse.Ok("", arrayVo);
    }

    @GetMapping("/qd/:listByIssue")
    @ApiOperation(value = "获取单期次推荐列表", response = QiangdanVo.QiangdanArrayVo.class)
    public ApiResponse listQiangdan(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestParam(required = false) String issueName,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode) {
        boolean shijiebei = DeviceType.Android.equals(deviceType) && Arrays.asList(162, 65).contains(appCode);
        // 默认查询期次处理
        // 若客户端没传，取当前最新期次
        if (Strings.isNullOrEmpty(issueName)) {
            issueName = newsQiangdanService.listLatestIssueArray().get(0);
        }
        QiangdanVo.QiangdanArrayVo arrayVo = new QiangdanVo.QiangdanArrayVo();
        List<NewsQiangdan> value = newsQiangdanService.list(issueName, token);
        if (Objects.nonNull(value) && !value.isEmpty()) {
            arrayVo.setCount(value.size());
            value.stream().map(it -> {
                if (shijiebei) {
                    it.setLeague(it.getLeague().replaceAll("世界杯", ""));
                }
                return it;
            }).forEach(it -> arrayVo.getValue().add(new QiangdanVo(it)));
        }


        return new ApiResponse.Ok("", arrayVo);
    }

    @GetMapping("/qd/:listAll")
    @ApiOperation(value = "获取推荐列表", response = QiangdanVo.QiangdanArrayWrapVo.class)
    public ApiResponse listAllQiangdan(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestHeader(required = false, value = HK_DEVICE_TYPE, defaultValue = "Android") DeviceType deviceType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode) {
        boolean shijiebei = DeviceType.Android.equals(deviceType) && Arrays.asList(162, 65).contains(appCode);
        QiangdanVo.QiangdanArrayWrapVo wrapperVo = new QiangdanVo.QiangdanArrayWrapVo();
        newsQiangdanService.listLatestIssueArray().forEach(it -> {
            QiangdanVo.QiangdanArrayVo arrayVo = new QiangdanVo.QiangdanArrayVo();
            List<NewsQiangdan> value = newsQiangdanService.list(it, token);
            if (Objects.nonNull(value) && !value.isEmpty()) {
                arrayVo.setIssue(it);
                arrayVo.setCount(value.size());
                value.stream().map(qd -> {
                    if (shijiebei) {
                        qd.setLeague(qd.getLeague().replaceAll("世界杯", ""));
                    }
                    return qd;
                }).forEach(qd -> arrayVo.getValue().add(new QiangdanVo(qd)));
                wrapperVo.getWrap().add(arrayVo);
            }
        });
        return new ApiResponse.Ok("", wrapperVo);
    }

    @GetMapping("/appcode")
    @ApiOperation(value = "通过AppCode获取应用名称", response = AppCodeVo.class)
    public ApiResponse appCode(@RequestParam int appCode) {
        return new ApiResponse.Ok("",
                new AppCodeVo(
                        appCode,
                        appCodeService.getAppNameByCode(appCode)));
    }

    @GetMapping("/yinglibaodian")
    @ApiOperation(value = "盈利宝典", response = NewsArrayVo.class)
    public ApiResponse listYinglibaodian(
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        return listNews("10041", appCode, channel, cityName, page, pageSize);
    }

    @GetMapping("/qingbaozhan")
    @ApiOperation(value = "情报", response = NewsArrayVo.class)
    public ApiResponse listQingbaozhan(
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        return listNews("10030", appCode, channel, cityName, page, pageSize);
    }

    @GetMapping("/newsFive")
    @ApiOperation(value = "最新五条情报", response = NewsArrayVo.class)
    public ApiResponse listNewsFive(
            @RequestHeader(required = false, defaultValue = "1", value = HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_CITY) String cityName,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) throws UnsupportedEncodingException {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("qingbaozhan");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        cityName = !Strings.isNullOrEmpty(cityName) ? URLDecoder.decode(cityName, "UTF8") : "";
        List<News> dboNewses = newsService.newsFive(appCode, channel, cityName);
        if (!dboNewses.isEmpty()) {
            dboNewses.forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }
}
