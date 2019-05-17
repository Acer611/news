package td.news.common;

import lombok.Getter;

/**
 * Created by root on 17-3-6.
 */
public class BizException extends RuntimeException {

    @Getter
    private int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(int code) {
        this.code = code;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
