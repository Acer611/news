package td.news.api.api.news;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.news.vo.*;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.news.NewsCommentService;
import td.news.biz.news.domain.NewsComment;
import td.news.biz.news.domain.SimpleComment;
import td.news.common.BizException;

import java.util.List;
import java.util.Objects;

import static td.news.common.ApiHeader.HK_TOKEN;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/news/comment")
@Api(tags = "news-comment", description = "资讯评论相关")
public class NewsCommentApi {

    @Autowired private TokenService tokenService;
    @Autowired private NewsCommentService newsCommentService;


    @PostMapping
    @ApiOperation(value = "评论", response = ApiResponse.Ok.class)
    public ApiResponse comment(
            @RequestHeader(HK_TOKEN) String token,
            @RequestBody NewsCommentRequestBody body) {
        long uid = tokenService.uid(token);
        newsCommentService.comment(uid, body.getId(), body.getContent());
        return new ApiResponse.Ok("评论成功");
    }

    @PostMapping("/remove")
    @ApiOperation(value = "删除", response = ApiResponse.Ok.class)
    public ApiResponse remove(
            @RequestHeader(HK_TOKEN) String token,
            @RequestBody NewsRemoveRequestBody body) {
        long uid = tokenService.uid(token);
        newsCommentService.delete(uid, body.getId());
        return new ApiResponse.Ok("操作成功");
    }

    @GetMapping("/:all")
    @ApiOperation(value = "评论列表", response = NewsCommentVo.NewsCommentArrayVo.class)
    public ApiResponse listAll(
            @RequestHeader(HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize) {
        long uid = tokenService.uid(token);
        NewsCommentVo.NewsCommentArrayVo arrayVo = new NewsCommentVo.NewsCommentArrayVo();
        List<NewsComment> value = newsCommentService.listAll(uid, page, pageSize);
        if (Objects.nonNull(value) && !value.isEmpty()) {
            value.forEach(
                    it ->
                            arrayVo.getValue().add(new NewsCommentVo(it))
            );
        }
        return new ObjApiResponse("", arrayVo);
    }

    @GetMapping("/:allByNews")
    @ApiOperation(value = "评论列表 <= 资讯下的评论", response = SimpleNewsCommentVo.SimpleNewsCommentArrayVo.class)
    public ApiResponse listAllByNews(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestParam long id,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize) {
        long uid = tokenService.uidWithoutCheck(token);
        SimpleNewsCommentVo.SimpleNewsCommentArrayVo arrayVo = new SimpleNewsCommentVo.SimpleNewsCommentArrayVo();
        List<SimpleComment> value = newsCommentService.listByNews(uid, id, page, pageSize);
        if (Objects.nonNull(value) && !value.isEmpty()) {
            value.forEach(
                    it ->
                            arrayVo.getValue().add(new SimpleNewsCommentVo(it, uid))
            );
        }
        return new ApiResponse.Ok("", arrayVo);
    }

    @GetMapping("/:allByNewsOnlyBySelf")
    @ApiOperation(value = "评论列表 <= 资讯下的评论 <= 仅展示我的", response = SimpleNewsCommentVo.SimpleNewsCommentArrayVo.class)
    public ApiResponse listAllByNewsOnlyBySelf(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestParam long id,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize) {
        long uid = tokenService.uidWithoutCheck(token);
        SimpleNewsCommentVo.SimpleNewsCommentArrayVo arrayVo = new SimpleNewsCommentVo.SimpleNewsCommentArrayVo();
        List<SimpleComment> value = newsCommentService.listByNewsOnlyBySelf(uid, id, page, pageSize);
        if (Objects.nonNull(value) && !value.isEmpty()) {
            long finalUid = uid;
            value.forEach(
                    it ->
                            arrayVo.getValue().add(new SimpleNewsCommentVo(it, finalUid))
            );
        }
        return new ApiResponse.Ok("", arrayVo);
    }
}
