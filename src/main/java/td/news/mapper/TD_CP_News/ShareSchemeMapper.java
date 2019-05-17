package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;

import java.util.Date;
import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface ShareSchemeMapper {

    List<DbShareScheme> queryAllSharedScheme(Date pageDate, RowBounds rowBounds);
    List<DbShareScheme> queryRegionConfiguredShareScheme(@Param("PageDate") Date pageDate, @Param("RegionMaster") List<Long> regionConfiguredMaster, RowBounds rowBounds);
    List<DbShareScheme> queryJhSharedScheme(Date pageDate, RowBounds rowBounds);
    List<DbShareScheme> queryRegionConfiguredJhSharedScheme(@Param("PageDate") Date pageDate, @Param("RegionMaster") List<Long> regionConfiguredMaster, RowBounds rowBounds);
    List<DbShareScheme> queryUserSharedScheme(long uid, RowBounds rowBounds);
    List<DbShareScheme> queryNonMasterSharedScheme(@Param("UserID") long uid, @Param("PageDate") Date pageDate, RowBounds rowBounds);
    DbShareScheme get(long id);
}
