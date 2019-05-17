package td.news.biz.news;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.news.domain.News;
import td.news.common.BizException;
import td.news.mapper.TD_CP_News.NewsCollectMapper;
import td.news.mapper.TD_CP_News.dbo.DbNews;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class NewsCollectService {

    @Autowired private NewsCollectMapper newsCollectMapper;

    public List<News> listCollected(long uid, int page, int pageSize) {
        List<DbNews> value
                = newsCollectMapper.listAllCollected(uid, new RowBounds((page - 1) * pageSize, pageSize));
        if (Objects.isNull(value) || value.isEmpty()) {
            return Collections.emptyList();
        }
        return value.stream().map(News::new).collect(Collectors.toList());
    }

    public boolean hasCollected(long uid, long newsId) {
        return newsCollectMapper.hasCollected(uid, newsId);
    }

    public void collect(long uid, long newId) {
        if (hasCollected(uid, newId)) {
            throw new BizException(17081401, "已经收藏过了");
        }
        newsCollectMapper.collect(uid, newId);
    }

    public void cancel(long uid, long newsId) {
        newsCollectMapper.cancel(uid, newsId);
    }
}
