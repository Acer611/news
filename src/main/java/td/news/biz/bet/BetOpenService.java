package td.news.biz.bet;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.client.open.BetOpenClient;
import td.news.client.open.vo.BetOpenVo;
import td.news.common.DeviceType;

import java.util.Objects;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class BetOpenService {

    @Autowired private BetOpenClient betOpenClient;

    /**
     * 云开关判断，是否禁售
     *
     * @param deviceType
     * @param deviceCode
     * @param appCode
     * @param version
     * @param cityName
     * @param channel
     * @return
     */
    public boolean isBan(DeviceType deviceType, String deviceCode, int appCode, String version, String cityName, String channel) {
//        if(true){
//            return false;
//        }
        // IOS过来的，全部非禁售
        if (DeviceType.iOS.equals(deviceType)) {
            return false;
        }
        JsonObject params = new JsonObject();
        params.addProperty("Platform", deviceType.getCode());
        params.addProperty("VerisonNo", version);
        params.addProperty("AppStore", channel);
        params.addProperty("Code", appCode);
        params.addProperty("City", cityName);
        params.addProperty("Device", deviceCode);
        try {
            BetOpenVo value = betOpenClient.status(params.toString());
            return Objects.nonNull(value) && Objects.nonNull(value.getData()) && value.getData().isIsOpen();
        } catch (Exception e) {
            log.error("params={}, error={}", params.toString(), e);
            return true;
        }
    }
}
