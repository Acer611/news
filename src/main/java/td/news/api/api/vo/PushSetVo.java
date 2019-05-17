package td.news.api.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import td.news.biz.PushSet;

/**
 * @author sanlion do
 */
@Data
public class PushSetVo {

    @ApiModelProperty("中奖推送") private int hit;
    @ApiModelProperty("开奖推送-双色球") private int ssq;
    @ApiModelProperty("开奖推送-大乐透") private int dlt;
    @ApiModelProperty("开奖推送-福彩3D") private int fc3d;
    @ApiModelProperty("开奖推送-排列3") private int pl3;
    @ApiModelProperty("进球推送") private int goal;
    @ApiModelProperty("大神推单推送") private int master;
    @ApiModelProperty("开播提醒") private int live;

    public PushSetVo(PushSet value) {

        BeanUtils.copyProperties(value, this);
    }
}
