package td.news.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.vo.PushSetRb;
import td.news.api.api.vo.PushSetVo;
import td.news.api.common.response.ApiResponse;
import td.news.biz.PushSet;
import td.news.biz.PushSettingService;
import td.news.biz.TokenService;
import td.news.common.AppType;

import java.util.List;

import static td.news.common.ApiHeader.HK_APP;
import static td.news.common.ApiHeader.HK_TOKEN;
import static td.news.repository.pushset.PushSettingRepository.allTags;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/system/set")
@Api(tags = "system-set", description = "设置")
public class PushSetApi {

    @Autowired private TokenService tokenService;
    @Autowired private PushSettingService pushSettingService;

    @PostMapping
    @ApiOperation(value = "推送设置", notes = "用1/0表示开/关", response = ApiResponse.Ok.class)
    public ApiResponse set(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType app,
            @RequestHeader(HK_TOKEN) String token,
            @ApiParam("设置") @RequestBody PushSetRb pushSet) {
        long uid = tokenService.uid(token);
        pushSettingService.pushset(uid, app,
                pushSet.getHit(), pushSet.getSsq(), pushSet.getDlt(), pushSet.getFc3d(), pushSet.getPl3(), pushSet.getGoal(), pushSet.getMaster(),
                pushSet.getLive());
        return new ApiResponse.Ok("设置成功");
    }

    @GetMapping
    @ApiOperation(value = "查询推送设置", notes = "用1/0表示开/关", response = PushSetVo.class)
    public ApiResponse get(
            @RequestHeader(HK_TOKEN) String token) {
        long uid = tokenService.uid(token);
        List<String> tags = pushSettingService.listTags(uid);
        PushSet set = new PushSet();
        if (!tags.isEmpty()) {
            set.setHit(tags.contains(allTags.get(0)) ? 1 : 0);
            set.setSsq(tags.contains(allTags.get(1)) ? 1 : 0);
            set.setDlt(tags.contains(allTags.get(2)) ? 1 : 0);
            set.setFc3d(tags.contains(allTags.get(3)) ? 1 : 0);
            set.setPl3(tags.contains(allTags.get(4)) ? 1 : 0);
            set.setGoal(tags.contains(allTags.get(5)) ? 1 : 0);
            set.setMaster(tags.contains(allTags.get(6)) ? 1 : 0);
            set.setLive(tags.contains(allTags.get(7)) ? 1 : 0);
        }
        return new ApiResponse.Ok("", new PushSetVo(set));
    }
}
