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
public class HandlerBaseVo<T> implements Serializable {

    public static final HandlerBaseVo Ok = new HandlerBaseVo();

    private static final long serialVersionUID = 8904951115784291463L;

    private int code;
    private String msg;
    T data;

    public boolean isOk() {
        return this.code == 0;
    }

    public boolean isFail() {
        return !isOk();
    }

    public T value(int failCode) {
        requireOk(failCode);
        return data;
    }

    public T value(int failCode, String failMessage) {
        requireOk(failCode, failMessage);
        return data;
    }

    /**
     * 若请求失败，则抛出异常bizException
     *
     * @param failCode
     * @param failMessage
     * @see BizException
     */
    public HandlerBaseVo<T> requireOk(int failCode, String failMessage) {
        if (isFail()) {
            HandlerBaseVo.log.warn(getMsg());
            throw new BizException(failCode, failMessage);
        }
        return this;
    }

    /**
     * 若请求失败，则抛出异常bizException
     *
     * @param failCode
     * @see BizException
     * @see HandlerBaseVo#requireOk(int, String)
     */
    public HandlerBaseVo<T> requireOk(int failCode) {
        return requireOk(failCode, msg);
    }
}
