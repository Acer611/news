package td.news.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import td.news.common.BizException;

import java.io.Serializable;

/**
 * Created by PC on 2017/3/20.
 */
@Slf4j
@Data
@NoArgsConstructor
public class Vo<T> implements Serializable {

    public static final Vo Ok = new Vo();

    private static final long serialVersionUID = 8904951115784291463L;

    private int ret;
    private String message;
    T output;

    public boolean isOk() {
        return this.ret > 0;
    }

    public boolean isFail() {
        return !isOk();
    }

    public T value(int failCode) {
        requireOk(failCode);
        return output;
    }

    public T value(int failCode, String failMessage) {
        requireOk(failCode, failMessage);
        return output;
    }

    /**
     * 若请求失败，则抛出异常bizException
     *
     * @param failCode
     * @param failMessage
     * @see BizException
     */
    public Vo<T> requireOk(int failCode, String failMessage) {
        if (isFail()) {
            Vo.log.warn(getMessage());
            throw new BizException(failCode, failMessage);
        }
        return this;
    }

    /**
     * 若请求失败，则抛出异常bizException
     *
     * @param failCode
     * @see BizException
     * @see Vo#requireOk(int, String)
     */
    public Vo<T> requireOk(int failCode) {
        return requireOk(failCode, message);
    }
}
