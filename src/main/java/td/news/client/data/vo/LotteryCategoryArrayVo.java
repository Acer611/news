package td.news.client.data.vo;

import lombok.Data;
import td.news.client.HandlerBaseVo;

import java.util.ArrayList;

@Data
public class LotteryCategoryArrayVo extends HandlerBaseVo<LotteryCategoryArrayVo.Body> {


    public static class Body extends ArrayList<LotteryCategoryVo> {


    }

    @Data
    public static class LotteryCategoryVo{

        int LotteryID;
        String LotteryName;
        String IconUrl;
        String Title;
    }
}
