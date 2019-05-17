package td.news.client.open.vo;

import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class BetOpenVo {

    private int code;
    private Body data;

    @Data
    public static class Body{

        private boolean IsOpen;
    }
}
