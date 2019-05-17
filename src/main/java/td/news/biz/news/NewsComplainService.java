package td.news.biz.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.mapper.TD_CP_News.NewsComplainMapper;
import td.news.repository.mbr.MbrRepository;

/**
 * @author sanlion do
 */
@Service
public class NewsComplainService {

    @Autowired private NewsComplainMapper newsComplainMapper;

    public void complain(long uid, long targetId, int targetType, String content) {
        newsComplainMapper.complaint(uid, targetId, targetType, content);
    }
}
