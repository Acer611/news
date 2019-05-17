package td.news.client.open;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import td.news.client.open.vo.BetOpenVo;

/**
 * @author sanlion do
 */
@FeignClient(name = "client-BetOpen", url = "${config.client.base}")
public interface BetOpenClient {


    @RequestMapping(value = "/?action=6")
    BetOpenVo status(@RequestParam("params") String params);
}
