package td.news.mapper.TD_CP_News;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import td.news.repository.DboNewsCategory;
import td.news.mapper.TD_CP_News.dbo.DbNews;

import java.util.List;

@Mapper
public interface NewsMapper {
    
    List<DbNews> queryLatestOneMonth(@Param("code") String code);
    List<DbNews> allActivity();
    List<DbNews> listCpxt();
    List<DbNews> allIndexActivity();


    List<DbNews> queryHelperNews(RowBounds rowBounds);
    
    List<DbNews> queryLatestTwoWeekHotNews();
    
    // 详情
    DbNews byId(long id);

    List<DboNewsCategory> listCategory();

    List<DbNews> queryNewsBySpecifyUrl(List<String> links);

    void countRpt(long newsid);

    List<DboNewsCategory> listV2Category();
}
