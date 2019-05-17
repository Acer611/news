package td.news.biz.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.repository.device.AppCodeRepository;
import td.news.repository.device.dbo.CacheAppCode;

/**
 * @author sanlion do
 */
@Service
public class AppCodeService {


    @Autowired private AppCodeRepository appCodeRepository;


    public String getAppNameByCode(int appCode) {
        CacheAppCode byAppCode = appCodeRepository.getByAppCode(appCode);
        return byAppCode.getAppName();
    }
}
