package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import td.news.mapper.TD_CP_News.dbo.DbNews;

import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface BannerMapper {

    List<DbNews> listByCode(
            @Param("appCode") String appCode,
            @Param("channel") String channel,
            @Param("cityName") String city,
            @Param("code") String code,
            @Param("UserTags") String userTag,
            @Param("LotteryID") String lottery);

    List<DbNews> listAllAvailableBanner(@Param("code") String code);

    List<DbNews> listAllBanner();

    List<DbNews> findAllAnnouncement(@Param("code") String code);
}
