package td.news.client.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import td.news.client.match.ClientMatchArrayVo;

@FeignClient(name = "client-Data", url = "${config.client.data}")
public interface DataClient {

    @RequestMapping(value = "/?action=404", method = RequestMethod.GET)
    ClientMatchArrayVo listMultiFootballMatches(@RequestParam("params") String params);
}
