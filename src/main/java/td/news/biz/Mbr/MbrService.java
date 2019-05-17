package td.news.biz.Mbr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.Mbr.domain.BaseUserInfo;
import td.news.repository.mbr.MbrRepository;
import td.news.repository.mbr.dbo.DbBaseUserInfo;

import java.util.Objects;

/**
 * @author sanlion do
 */
@Slf4j
@Service
public class MbrService {

    @Autowired private MbrRepository mbrRepository;

    public BaseUserInfo getById(long uid) {
        DbBaseUserInfo value = mbrRepository.getBaseUser(uid);
        return Objects.nonNull(value) ? new BaseUserInfo(value) : null;
    }
}
