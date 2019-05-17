package td.news.client.match;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author sanlion do
 */
@FeignClient(name = "client-MasterScheme", url = "${config.client.user}")
public interface MasterSchemeClient {

    @RequestMapping(value = "/?action=1503", method = RequestMethod.GET)
    ClientQiangdanPayIssueNoArrayVo qiangdanPayIssueNo(@RequestParam("params") String params);

    @RequestMapping(value = "/?action=518", method = RequestMethod.GET)
    BetCheckVo BetCheck(@RequestParam("params") String params);

}
