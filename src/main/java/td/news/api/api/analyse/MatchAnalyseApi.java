package td.news.api.api.analyse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.analyse.vo.AnalyseVo;
import td.news.api.api.analyse.vo.AnalyseWrapperVo;
import td.news.api.api.analyse.vo.MatchLiveVo;
import td.news.api.common.response.ApiResponse;
import td.news.biz.spider.FootballLiveSpiderService;
import td.news.biz.spider.MatchAnalyseSpiderService;
import td.news.repository.news.NewsRepository;
import td.news.repository.spider.dbo.CacheLiveUrl;
import td.news.repository.spider.dbo.DboAnalyse;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static td.news.common.ApiHeader.HK_STAMP;

@Slf4j
@RestController
@RequestMapping("/football/analyse")
@Api(tags = "analyse", description = "赛事分析")
public class MatchAnalyseApi {

    @Autowired private NewsRepository newsRepository;
    @Autowired private MatchAnalyseSpiderService matchAnalyseSpiderService;
    @Autowired private FootballLiveSpiderService footballLiveSpiderService;

    @GetMapping
    @ApiOperation(value = "获取分析概述数据", response = AnalyseVo.class)
    public ApiResponse analyse(@ApiParam("IssueNo") @RequestParam String issueNo) {
        DboAnalyse analyse = matchAnalyseSpiderService.analyse(issueNo);
        if (Objects.isNull(analyse)) {
            return new ApiResponse.Fail(true, 17101001, "暂无赛事分析");
        }
        return new ApiResponse.Ok("", new AnalyseVo(analyse));
    }

    @GetMapping("/all")
    @ApiOperation(value = "获取分析概述数据", response = AnalyseWrapperVo.class)
    public ApiResponse analyse(@RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        long latestTimestamp = newsRepository.getTimestamp("analyse", 5, TimeUnit.MINUTES);
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中<<<<<<<赛事分析<< /football/analyse/all");
            return new ApiResponse.Ok(latestTimestamp);
        }

        AnalyseWrapperVo analyseWrapperVo = new AnalyseWrapperVo();
        List<DboAnalyse> analyses = matchAnalyseSpiderService.analyse();
        if (!Objects.isNull(analyses)) {
            analyses.forEach(it -> analyseWrapperVo.getAnalyses().put(it.getIssueNO(), new AnalyseVo(it)));
        }
        return new ApiResponse.Ok("", analyseWrapperVo, latestTimestamp);
    }

    @GetMapping("/live")
    @ApiOperation(value = "获取动画直播播放地址", response = MatchLiveVo.class)
    public ApiResponse liveUrl(@ApiParam("IssueNo") @RequestParam String issueNo) {
        CacheLiveUrl value = footballLiveSpiderService.getLiveUrl(issueNo);
        if (Objects.isNull(value)) {
            return new ApiResponse.Fail(true, 18010301, "暂无动画直播");
        }
        return new ApiResponse.Ok("", new MatchLiveVo(value.getLiveUrl(), value.getLiveUrl4H5(), value.getReferer()));
    }
}
