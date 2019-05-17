package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface LikeMapper {

    boolean hasLiked(@Param("Target") long target, @Param("UserID") long uid);
    List<Long> listLikedTarget(@Param("Target") List<Long> target, @Param("UserID") long uid);
}
