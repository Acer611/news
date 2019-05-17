package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import td.news.mapper.TD_CP_News.dbo.DbNews;

import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface LaunchConfigMapper {

    List<DbNews> listAll(
            @Param("appCode") String appCode,
            @Param("channel") String channel,
            @Param("cityName") String city,
            @Param("UserTags") String userTag);

    List<DbNews> listAllAvailable();
}
