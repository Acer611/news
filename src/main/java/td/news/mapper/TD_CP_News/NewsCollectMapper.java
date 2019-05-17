package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import td.news.mapper.TD_CP_News.dbo.DbNews;

import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface NewsCollectMapper {

    void collect(@Param("uid") long uid, @Param("newsid") long newsId);

    void cancel(@Param("uid") long uid, @Param("newsid") long newsId);

    List<DbNews> listAllCollected(@Param("uid") long uid, RowBounds rowBounds);
    List<DbNews> listAllCollected(@Param("uid") long uid);

    boolean hasCollected(@Param("uid") long uid, @Param("newid") long newsid);
}
