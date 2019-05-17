package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;

import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface ShareSchemeSnapshotMapper {

    List<DbShareScheme> queryJhSharedScheme(RowBounds rowBounds);
}
