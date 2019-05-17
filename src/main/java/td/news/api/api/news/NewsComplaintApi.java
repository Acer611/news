package td.news.api.api.news;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.news.vo.ComplainRequestBody;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.news.NewsComplainService;

import static td.news.common.ApiHeader.HK_TOKEN;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/news/complain")
@Api(tags = "news-complain", description = "举报")
public class NewsComplaintApi {

    @Autowired private NewsComplainService newsComplainService;
    @Autowired private TokenService tokenService;

    @PostMapping
    @ApiOperation(
            value = "举报",
            notes = "点赞类型 1.资讯评论 2.花椒直播彩帝 3.彩帝订单 4.彩帝订单评论 5.晒单",
            response = ApiResponse.Ok.class)
    public ApiResponse complain(
            @RequestHeader(HK_TOKEN) String token,
            @RequestBody ComplainRequestBody body) {
        long uid = tokenService.uid(token);
        newsComplainService.complain(uid, body.getTarget(), body.getTargetType(), body.getContent());
        return new ApiResponse.Ok("举报成功");
    }
}
