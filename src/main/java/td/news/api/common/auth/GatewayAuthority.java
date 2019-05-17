package td.news.api.common.auth;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import static td.news.common.ApiHeader.*;

//import izuanqian.biz.account.TokenService;

/**
 * Created by root on 17-3-7.
 */
@Service
public class GatewayAuthority {

    @Value("${config.api.auth.sign.salt}") private String salt;
    @Value("${config.api.auth.time}") private int validSecond;
//    @Autowired private TokenService tokenService;

    /**
     * check whether sign is ok
     *
     * @param request
     * @return
     */
    public boolean isSignValid(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> signArgs = new TreeMap<>();
        String sign = null;
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            System.out.println(key);
            if (Arrays.asList(HK_TIMESTAMP, HK_TOKEN).contains(key)) {
                signArgs.put(key, request.getHeader(key));
            }
            if (HK_SIGN.equals(key)) {
                sign = request.getHeader(key);
            }
        }
        if (Strings.isNullOrEmpty(sign)) {
            return false;
        }
        StringBuffer sb = new StringBuffer(salt);
        signArgs.forEach((key, value) -> sb.append(value));

        return sign.
                equalsIgnoreCase(
                        Hashing
                                .md5()
                                .newHasher()
                                .putString(sb.toString(), Charset.forName("UTF-8"))
                                .hash()
                                .toString()
                );
    }

    /**
     * Verify that the request is within the specified time period
     *
     * @param request
     * @return
     */
    public boolean isTimeValid(HttpServletRequest request) {
        String timestampStr = request.getHeader(HK_TIMESTAMP);
        if (Strings.isNullOrEmpty(timestampStr)) {
            return false;
        }
        if (!Arrays.asList(10, 13).contains(timestampStr.length())) {
            return false;
        }
        timestampStr = timestampStr.length() == 10 ? timestampStr + "000" : timestampStr;
        long timestamp;
        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (Exception e) {
            return false;
        }
        System.out.println(timestamp);
        long now = System.currentTimeMillis();
        return Math.abs(now - timestamp) < validSecond * 1000;
    }

    /**
     * 校验token的正确性
     *
     * @param request
     * @return
     */
    public void validToken(HttpServletRequest request) {
        /*
        当且仅当token有值时，才验证
         */
        String token = request.getHeader(HK_TOKEN);
        if (!Strings.isNullOrEmpty(token)) {
//            tokenService.getUidByToken(token);
        }
    }
}
