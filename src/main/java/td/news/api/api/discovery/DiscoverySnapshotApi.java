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
import td.news.biz.discovery.ShareSchemeSnapshotService;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;

import java.util.List;

import static td.news.api.api.discovery.ShareSchemeSnapshotApi.Timestamp;
import static td.news.common.ApiHeader.HK_STAMP;
import static td.news.common.ApiHeader.HK_TOKEN;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/snapshot/discovery")
@Api(tags = "snapshot-discovery", description = "发现")
public class DiscoverySnapshotApi {

    @Autowired private ShareSchemeSnapshotService shareSchemeSnapshotService;
    @Autowired private TokenService tokenService;

    @SneakyThrows
    @GetMapping("/index")
    @ApiOperation(value = "分页获取晒单+资讯", response = DiscoveryWrapperVo.DiscoveryWrapperArrayVo.class)
    public ApiResponse index(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        // discovery_index_share

        DiscoveryWrapperVo.DiscoveryWrapperArrayVo discoveryWrapperArrayVo = new DiscoveryWrapperVo.DiscoveryWrapperArrayVo();

        long latestTimestamp = page == 1 ? Timestamp : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok("", discoveryWrapperArrayVo, latestTimestamp);
        }


        long self = tokenService.uidWithoutCheck(token);

        List<DbShareScheme> all = shareSchemeSnapshotService.page(page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> discoveryWrapperArrayVo.getValue().add(new DiscoveryWrapperVo(it, self)));
        }
        return new ApiResponse.Ok("", discoveryWrapperArrayVo, latestTimestamp);
    }


}
