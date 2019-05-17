package td.news.api.api.device;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.message.MessageService;
import td.news.common.AppType;
import td.news.common.DeviceType;

import static td.news.common.ApiHeader.*;


/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/device")
@Api(tags = "device", description = "设备号维护")
public class OnlineDeviceApi {

    @Autowired private TokenService tokenService;
    @Autowired private MessageService messageService;

    @ApiOperation(
            value = "设备号录入",
            notes = "登陆成功之后回调，记录在线设备",
            response = ApiResponse.Ok.class)
    @PostMapping
    public ApiResponse logLogin(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(HK_DEVICE_TYPE) DeviceType deviceType,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_PUSH_DEVICE_CODE) String pushDeviceCode,
            @RequestHeader(HK_TOKEN) String token,
            @RequestHeader(HK_APPCODE) int appCode,
            @RequestHeader(required = false, value = HK_BRAND) String brand,
            @RequestHeader(required = false) String bundleID,
            @RequestHeader(HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_MOBILE_CODE) String mobileCode,
            @RequestHeader(required = false, value = HK_VERSION) String version) {
        if (Strings.isNullOrEmpty(token)) {
            // 若没登陆，则不做处理，直接返回
            return new ApiResponse.Ok();
        }
        long uid = tokenService.uidWithoutCheck(token);
        if (uid <= 0) {
            return new ApiResponse.Ok();
        }
        messageService.logOnline(token, uid, appCode, brand, deviceType, bundleID, deviceCode, pushDeviceCode, mobileCode, channel, version, app);
        return new ApiResponse.Ok();
    }

    @PostMapping("/:init")
    @ApiOperation(value = "初始化设备信息 <= 设备启动调用", response = ApiResponse.Ok.class)
    public ApiResponse init(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(HK_DEVICE_TYPE) DeviceType deviceType,
            @RequestHeader(required = false, value = HK_DEVICE_CODE) String deviceCode,
            @RequestHeader(required = false, value = HK_PUSH_DEVICE_CODE) String pushDeviceCode,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @RequestHeader(HK_APPCODE) int appCode,
            @RequestHeader(HK_CHANNEL) String channel,
            @RequestHeader(required = false, value = HK_VERSION) String version,
            @RequestHeader(required = false, value = HK_MOBILE_CODE) String mobileCode,
            @RequestHeader(required = false, value = HK_BRAND) String brand,
            @RequestHeader(required = false) String bundleID) {

        long uid = tokenService.uidWithoutCheck(token);
        messageService.logOnline(token, uid, appCode, brand, deviceType, bundleID, deviceCode, pushDeviceCode, mobileCode, channel, version, app);
        return new ApiResponse.Ok();
    }

    @ApiIgnore
    @PostMapping("/sync")
    @ApiOperation(
            value = "更新推送设备号",
            notes = "设备获得推送token之后，同步到系统",
            response = ApiResponse.Ok.class)
    public ApiResponse sync() {
        // todo 去除掉可能重复的业务接口
        return new ApiResponse.Ok();
    }
}
