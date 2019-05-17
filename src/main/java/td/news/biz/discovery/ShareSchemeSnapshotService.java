package td.news.biz.discovery;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import td.news.common.AppType;
import td.news.configuration.CacheConfiguration;
import td.news.mapper.TD_CP_News.ShareSchemeSnapshotMapper;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;
import td.news.repository.discovery.SchemeShareSnapshotRepository;

import java.util.Arrays;
import java.util.List;

import static td.news.configuration.CacheConfiguration.bizCache_10_min;

/**
 * @author sanlion do
 */
@Service
public class ShareSchemeSnapshotService {

    @Autowired private ShareSchemeSnapshotMapper shareSchemeSnapshotMapper;
    @Autowired private SchemeShareSnapshotRepository schemeShareSnapshotRepository;

    @Cacheable(value = bizCache_10_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbShareScheme> page(int page, int pageSize){
        return schemeShareSnapshotRepository.page(page, pageSize);
    }

    @Cacheable(value = CacheConfiguration.bizCache_30_min, keyGenerator = "simpleMethodNameKeyGenerator")
    public List<DbShareScheme> listAllJh(int page, int pageSize) {
        return shareSchemeSnapshotMapper.queryJhSharedScheme(new RowBounds((page - 1) * pageSize, pageSize));
    }


    public List<DbShareScheme> pageShareScheme(long uid, int page, int pageSize) {
        return schemeShareSnapshotRepository.page(uid, page, pageSize);
    }

    public boolean snapshotHit(AppType appType, int appCode) {
        if (!AppType.Caipiao.equals(appType)) {
            return false;
        }
        return !Arrays.asList(169).contains(appCode);
    }
}
