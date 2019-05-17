package td.news.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.common.BizException;
import td.news.repository.mbr.TokenRepository;

@Service
public class TokenService {

    @Autowired private TokenRepository tokenRepository;

    public long uid(String token) {
        return tokenRepository.uid(token);
    }

    public long uidWithoutCheck(String token) {
        try {
            return uid(token);
        } catch (BizException e) {
            return 0;
        }
    }
}
