package td.news.api.api.news;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.news.vo.NewsArrayVo;
import td.news.api.api.news.vo.NewsCollectRequestBody;
import td.news.api.api.news.vo.ObjApiResponse;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.news.NewsCollectService;
import td.news.biz.news.domain.News;
import td.news.common.BizException;

import java.util.List;
import java.util.Objects;

import static td.news.common.ApiHeader.HK_TOKEN;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/news/collect")
@Api(tags = "news-collect", description = "资讯收藏相关")
public class NewsCollectApi {

    @Autowired private TokenService tokenService;
    @Autowired private NewsCollectService newsCollectService;


    @PostMapping
    @ApiOperation(value = "收藏", response = ApiResponse.Ok.class)
    public ApiResponse collect(
            @RequestHeader(HK_TOKEN) String token,
            @RequestBody NewsCollectRequestBody body) {
        long uid = tokenService.uid(token);
        newsCollectService.collect(uid, body.getId());
        return new ApiResponse.Ok("收藏成功");
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消收藏", response = ApiResponse.Ok.class)
    public ApiResponse cancel(
            @RequestHeader(HK_TOKEN) String token,
            @RequestBody NewsCollectRequestBody body) {
        long uid = tokenService.uid(token);
        newsCollectService.cancel(uid, body.getId());
        return new ApiResponse.Ok("取消收藏成功");
    }

    @GetMapping("/check")
    @ApiOperation(value = "校验是否已收藏", response = ApiResponse.Ok.class)
    public ApiResponse collectState(
            @RequestHeader(HK_TOKEN) String token,
            @RequestParam long id) {
        long uid = tokenService.uidWithoutCheck(token);
        boolean hasCollected = uid > 0 && newsCollectService.hasCollected(uid, id);
        if (hasCollected) {
            throw new BizException(17081402, "已收藏");
        }
        return new ApiResponse.Ok();
    }

    @GetMapping("/:all")
    @ApiOperation(value = "分页查询资讯收藏列表", response = NewsArrayVo.class)
    public ApiResponse listAllCollectedNews(
            @RequestHeader(HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize) {
        NewsArrayVo arrayVo = new NewsArrayVo();
        List<News> value = newsCollectService.listCollected(tokenService.uid(token), page, pageSize);
        if (Objects.nonNull(value) && !value.isEmpty()) {
            value.forEach(it -> arrayVo.getNewses().add(new NewsArrayVo.NewsVo(it)));
        }
        return new ObjApiResponse("", arrayVo);
    }

}
