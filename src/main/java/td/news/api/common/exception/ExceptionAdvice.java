package td.news.api.common.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import td.news.api.common.response.ApiResponse;
import td.news.common.BizException;


/**
 * Created by root on 17-3-6.
 */
@Slf4j
@RestController
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    ApiResponse defaultExceptionHandler(Exception exception) {
        log.error("", exception);
        return new ApiResponse.Fail(false, 17030601, "开小差中...");
    }

    @ExceptionHandler(BizException.class)
    ApiResponse bizExceptionHandler(BizException exception) {
        log.info("", exception.getMessage());
        return new ApiResponse.Fail(true, exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ApiResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        log.error("", exception);
        ObjectError error = exception.getBindingResult().getAllErrors().stream().findFirst().get();
        String message = error.getDefaultMessage();
        return new ApiResponse.Fail(true, 17032401, message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ApiResponse missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.error("", exception);
        return new ApiResponse.Fail(false, 17032801, "[" + exception.getParameterName() + "]字段必填");
    }
}
