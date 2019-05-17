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

import static td.news.common.ApiHeader.HK_STAMP;
import static td.news.common.ApiHeader.HK_TOKEN;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/snapshot/discovery/share")
@Api(tags = "snapshot-discovery-share", description = "晒单")
public class ShareSchemeSnapshotApi {

    @Autowired private TokenService tokenService;
    @Autowired private ShareSchemeSnapshotService shareSchemeSnapshotService;
    public static final long Timestamp = 1534326370596L;

    @SneakyThrows
    @GetMapping("/all")
    @ApiOperation(value = "分页获取晒单", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse index(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        long latestTimestamp = page == 1 ? Timestamp : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uidWithoutCheck(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all = shareSchemeSnapshotService.page(page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> shareSchemeArrayVo.getValue().add(new DiscoveryWrapperVo.ShareSchemeVo(it, self)));
        }
        return new ApiResponse.Ok("", shareSchemeArrayVo, latestTimestamp);
    }

    @SneakyThrows
    @GetMapping("/jh")
    @ApiOperation(value = "分页获取精华晒单", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse jh(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        long latestTimestamp = page == 1 ? Timestamp : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uidWithoutCheck(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all =  shareSchemeSnapshotService.listAllJh(page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it ->
                    shareSchemeArrayVo.getValue().add(new DiscoveryWrapperVo.ShareSchemeVo(it, self)));
        }
        return new ApiResponse.Ok("", shareSchemeArrayVo, latestTimestamp);
    }

    @SneakyThrows
    @GetMapping("/bySelf")
    @ApiOperation(value = "分页获取我的晒单", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse bySelf(
            @RequestHeader(HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {


        long latestTimestamp = page == 1 ? Timestamp : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uid(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all = shareSchemeSnapshotService.pageShareScheme(self, page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> shareSchemeArrayVo.getValue().add(new DiscoveryWrapperVo.ShareSchemeVo(it, self)));
        }
        return new ApiResponse.Ok("", shareSchemeArrayVo, latestTimestamp);
    }

    @SneakyThrows
    @GetMapping("/byMaster")
    @ApiOperation(value = "分页获取彩帝晒单<= 普通弹", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse byMaster() {
        return new ApiResponse.Ok("", new DiscoveryWrapperVo.ShareSchemeArrayVo());
    }

}
