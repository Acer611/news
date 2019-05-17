package td.news.api.api.discovery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import td.news.api.api.discovery.vo.DiscoveryWrapperVo;
import td.news.api.common.response.ApiResponse;
import td.news.biz.TokenService;
import td.news.biz.discovery.ShareSchemeService;
import td.news.biz.discovery.ShareSchemeSnapshotService;
import td.news.common.AppType;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;
import td.news.repository.news.NewsRepository;

import java.util.List;

import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/discovery")
@Api(tags = "discovery", description = "发现")
public class DiscoveryApi {

    @Autowired private ShareSchemeService shareSchemeService;
    @Autowired private TokenService tokenService;
    @Autowired private NewsRepository newsRepository;
    @Autowired private ShareSchemeSnapshotService shareSchemeSnapshotService;
    @Autowired private DiscoverySnapshotApi discoverySnapshotApi;

    @SneakyThrows
    @GetMapping("/index")
    @ApiOperation(value = "分页获取晒单+资讯", response = DiscoveryWrapperVo.DiscoveryWrapperArrayVo.class)
    public ApiResponse index(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType appType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        if (shareSchemeSnapshotService.snapshotHit(appType, appCode)) {
            return discoverySnapshotApi.index(token, page, pageSize, stamp);
        }

        DiscoveryWrapperVo.DiscoveryWrapperArrayVo discoveryWrapperArrayVo = new DiscoveryWrapperVo.DiscoveryWrapperArrayVo();

        long latestTimestamp = page == 1 ? newsRepository.getLatestTimestamp("discovery_index_share") : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("发现首页晒单，时间戳命中");
            return new ApiResponse.Ok("", discoveryWrapperArrayVo, latestTimestamp);
        }


        long self = tokenService.uidWithoutCheck(token);

        List<DbShareScheme> all = shareSchemeService.page(page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> discoveryWrapperArrayVo.getValue().add(new DiscoveryWrapperVo(it, self)));
        }
        return new ApiResponse.Ok("", discoveryWrapperArrayVo, latestTimestamp);
    }


}
