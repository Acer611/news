package td.news.api.api.news.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import td.news.api.common.response.ApiResponse;

/**
 * @author sanlion do
 */
@Data
public class ObjApiResponse extends ApiResponse.Ok{

    @ApiModelProperty("object data") private Object obj = Null;
    private long stamp;

    public ObjApiResponse(String message) {
        super();
        this.setMsg(message);
    }

    public ObjApiResponse(String message, Object obj) {
        this(message);
        this.setData(obj);
        this.obj = obj;
    }

    public ObjApiResponse(String message, Object obj, long stamp) {
        this(message);
        this.setData(obj);
        this.obj = obj;
        this.setStamp(stamp);
    }
}
