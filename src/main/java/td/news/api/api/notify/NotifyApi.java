package td.news.api.api.notify;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import td.news.api.common.response.ApiResponse;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/notify")
@Api(tags = "notify-rpt", description = "消息统计")
public class NotifyApi {



    @PostMapping("/callback/arrive")
    @ApiOperation(value = "消息到达回调", response = ApiResponse.Ok.class)
    public ApiResponse arriveCallback(
            @RequestBody MessageCallbackRequestBody body) {
//        try {
//            notificationRptService.arriveCallback(Long.parseLong(body.getId()));
//        } catch (Exception e) {
//            log.error("消息到达回调出错{}", body);
//        }
        return new ApiResponse.Ok();
    }

    @PostMapping("/callback/click")
    @ApiOperation(value = "消息点击回调", response = ApiResponse.Ok.class)
    public ApiResponse clickCallback(
            @RequestBody MessageCallbackRequestBody body) {
//        try {
//            notificationRptService.clickCallback(Long.parseLong(body.getId()));
//        } catch (Exception e) {
//            log.error("消息点击回调出错{}", body);
//        }
        return new ApiResponse.Ok();
    }
}
