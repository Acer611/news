package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author sanlion do
 */
@Mapper
public interface NewsCommentMapper {

    List<DbComment> listAll(long uid, RowBounds rowBounds);

    void comment(
            @Param("uid") long uid, @Param("newsid") long newsid, @Param("content") String content,
            @Param("nick") String nick, @Param("avatar") String avatar, @Param("username") String username, @Param("AuditState") int auditState);

    void delete(@Param("uid") long uid, @Param("id") long id);

    List<DbSimpleComment> listByNews(
            @Param("UserID") long uid,
            @Param("NewsID") long newsid,
            RowBounds rowBounds);

    List<DbSimpleComment> listByNewsOnlyBySelf(
            @Param("UserID") long uid,
            @Param("NewsID") long newsid,
            RowBounds rowBounds);
}
