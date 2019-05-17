package td.news.api.api.news;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.news.vo.NewsArrayVo;
import td.news.api.common.response.ApiResponse;
import td.news.biz.news.HelpService;
import td.news.biz.news.domain.News;
import td.news.repository.news.NewsRepository;

import java.util.List;

import static td.news.common.ApiHeader.HK_STAMP;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/help")
@Api(tags = "help", description = "帮助")
public class HelpApi {

    @Autowired private HelpService helpService;
    @Autowired private NewsRepository newsRepository;

    @GetMapping
    @ApiOperation(
            value = "帮助", response = NewsArrayVo.class)
    public ApiResponse newses(
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {
        NewsArrayVo vo = new NewsArrayVo();

        long latestTimestamp = newsRepository.getLatestTimestamp("help");
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("时间戳命中");
            return new ApiResponse.Ok("", vo, latestTimestamp);
        }

        List<News> dboNewses = helpService.queryHelpArray(page, pageSize);
        if (!dboNewses.isEmpty()) {
            dboNewses.stream()
                    .forEach(news -> vo.getNewses().add(new NewsArrayVo.NewsVo(news)));
        }
        return new ApiResponse.Ok("", vo, latestTimestamp);
    }
}
