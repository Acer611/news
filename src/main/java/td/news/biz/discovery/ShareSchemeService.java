package td.news.biz.discovery;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import td.news.configuration.CacheConfiguration;
import td.news.mapper.TD_CP_News.ShareSchemeMapper;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;
import td.news.repository.discovery.SchemeShareRepository;

import java.util.Date;
import java.util.List;

import static td.news.configuration.CacheConfiguration.bizCache_10_min;

/**
 * @author sanlion do
 */
@Service
public class ShareSchemeService {

    @Autowired private ShareSchemeMapper shareSchemeMapper;
    @Autowired private SchemeShareRepository schemeShareRepository;

    public DbShareScheme get(long id) {
        return schemeShareRepository.get(id);
    }

    @Cacheable(value = bizCache_10_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbShareScheme> page(int page, int pageSize){
        return schemeShareRepository.page(page, pageSize);
    }

    @Cacheable(value = CacheConfiguration.bizCache_30_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbShareScheme> listAllJh(int page, int pageSize) {
        return shareSchemeMapper.queryJhSharedScheme(new Date(), new RowBounds((page - 1) * pageSize, pageSize));
    }

    @Cacheable(value = CacheConfiguration.bizCache_60_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbShareScheme> pageNormalShareScheme(long uid, int page, int pageSize) {
        return schemeShareRepository.pageNormal(uid, page, pageSize);
    }

    public List<DbShareScheme> pageShareScheme(long uid, int page, int pageSize) {
        return schemeShareRepository.page(uid, page, pageSize);
    }
}
