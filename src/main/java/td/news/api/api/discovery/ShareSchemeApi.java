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
import td.news.biz.like.LikeService;
import td.news.common.AppType;
import td.news.common.BizException;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;
import td.news.repository.news.NewsRepository;

import java.util.List;
import java.util.Objects;

import static td.news.common.ApiHeader.*;

/**
 * @author sanlion do
 */
@Slf4j
@RestController
@RequestMapping("/discovery/share")
@Api(tags = "discovery-share", description = "晒单")
public class ShareSchemeApi {

    @Autowired private TokenService tokenService;
    @Autowired private ShareSchemeService shareSchemeService;
    @Autowired private LikeService likeService;
    @Autowired private NewsRepository newsRepository;
    @Autowired private ShareSchemeSnapshotService shareSchemeSnapshotService;
    @Autowired private ShareSchemeSnapshotApi shareSchemeSnapshotApi;

    @SneakyThrows
    @GetMapping("/all")
    @ApiOperation(value = "分页获取晒单", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse index(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType appType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {


        if (shareSchemeSnapshotService.snapshotHit(appType, appCode)) {
            return shareSchemeSnapshotApi.index(token, page, pageSize, stamp);
        }

        long latestTimestamp = page == 1 ? newsRepository.getLatestTimestamp("discovery_index_share") : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("晒单，时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uidWithoutCheck(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all = shareSchemeService.page(page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> shareSchemeArrayVo.getValue().add(new DiscoveryWrapperVo.ShareSchemeVo(it, self)));
        }
        return new ApiResponse.Ok("", shareSchemeArrayVo, latestTimestamp);
    }

    @SneakyThrows
    @GetMapping("/jh")
    @ApiOperation(value = "分页获取精华晒单", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse jh(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType appType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        if (shareSchemeSnapshotService.snapshotHit(appType, appCode)) {
            return shareSchemeSnapshotApi.jh(token, page, pageSize, stamp);
        }

        long latestTimestamp = page == 1 ? newsRepository.getLatestTimestamp("discovery_index_share") : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("晒单，时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uidWithoutCheck(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all = shareSchemeService.listAllJh(page, pageSize);
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
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType appType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @RequestHeader(HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {

        if (shareSchemeSnapshotService.snapshotHit(appType, appCode)) {
            return shareSchemeSnapshotApi.bySelf(token, page, pageSize, stamp);
        }

        long latestTimestamp = page == 1 ? newsRepository.getLatestTimestamp("discovery_index_share") : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("晒单，时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uid(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all = shareSchemeService.pageShareScheme(self, page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> shareSchemeArrayVo.getValue().add(new DiscoveryWrapperVo.ShareSchemeVo(it, self)));
        }
        return new ApiResponse.Ok("", shareSchemeArrayVo, latestTimestamp);
    }

    @SneakyThrows
    @GetMapping("/byMaster")
    @ApiOperation(value = "分页获取彩帝晒单<= 普通弹", response = DiscoveryWrapperVo.ShareSchemeArrayVo.class)
    public ApiResponse byMaster(
            @RequestHeader(required = false, value = HK_APP, defaultValue = "Caipiao") AppType appType,
            @RequestHeader(required = false, value = HK_APPCODE, defaultValue = "3") int appCode,
            @ApiParam("彩帝ID") @RequestParam long di,
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam("页面l大小") @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestHeader(required = false, value = HK_STAMP, defaultValue = "0") long stamp) {


        if (shareSchemeSnapshotService.snapshotHit(appType, appCode)) {
            return shareSchemeSnapshotApi.byMaster();
        }

        long latestTimestamp = page == 1 ? newsRepository.getLatestTimestamp("discovery_index_share") : 0;
        if (latestTimestamp != 0 && latestTimestamp == stamp) {
            log.debug("晒单，时间戳命中");
            return new ApiResponse.Ok(latestTimestamp);
        }

        long self = tokenService.uidWithoutCheck(token);
        DiscoveryWrapperVo.ShareSchemeArrayVo shareSchemeArrayVo = new DiscoveryWrapperVo.ShareSchemeArrayVo();
        List<DbShareScheme> all = shareSchemeService.pageNormalShareScheme(di, page, pageSize);
        if (!all.isEmpty()) {
            all.forEach(it -> shareSchemeArrayVo.getValue().add(new DiscoveryWrapperVo.ShareSchemeVo(it, self)));
        }
        return new ApiResponse.Ok("", shareSchemeArrayVo, latestTimestamp);
    }

    @SneakyThrows
    @GetMapping("/get")
    @ApiOperation(value = "详情", response = DiscoveryWrapperVo.ShareSchemeVo.class)
    public ApiResponse get(
            @RequestHeader(required = false, value = HK_TOKEN) String token,
            @ApiParam("晒单ID") @RequestParam long id) {
        long self = tokenService.uidWithoutCheck(token);
        DbShareScheme dbShareScheme = shareSchemeService.get(id);
        if (Objects.isNull(dbShareScheme)) {
            throw new BizException(17122901, "无法获取晒单详情");
        }
        DiscoveryWrapperVo.ShareSchemeVo obj = new DiscoveryWrapperVo.ShareSchemeVo(dbShareScheme, self);
        obj.setLiked(likeService.hasLiked(5, dbShareScheme.getID(), self));
        return new ApiResponse.Ok("", obj);
    }

}
