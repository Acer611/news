package td.news.api.api.feedback;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.feedback.vo.SchemeFeedbackBody;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.feedback.FeedbackService;

import static td.news.common.ApiHeader.HK_TOKEN;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/feedback")
@Api(tags = "feedback", description = "意见反馈")
public class FeedbackApi {

    @Autowired private TokenService tokenService;
    @Autowired private FeedbackService feedbackService;


    @PostMapping("/gteByScheme")
    @ApiOperation(value = "提交订单反馈", response = ApiResponse.Ok.class)
    public ApiResponse comment(
            @RequestHeader(HK_TOKEN) String token,
            @RequestBody SchemeFeedbackBody body) {
        long uid = tokenService.uid(token);
        feedbackService.feedbackByScheme(uid, body.getSchemeID(), body.getContent());
        return new ApiResponse.Ok("反馈已提交");
    }
}
