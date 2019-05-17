package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author sanlion do
 */
@Mapper
public interface NewsComplainMapper {

    void complaint(@Param("uid") long uid, @Param("target") long target, @Param("type") int type, @Param("content") String content);
}
