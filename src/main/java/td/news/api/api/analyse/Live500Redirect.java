package td.news.api.api.analyse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sanlion do
 */
@Controller
@RequestMapping("/500")
public class Live500Redirect {


    @GetMapping
    public String redirect() {
        return "redirect:https://live.m.500.com/center/football?render=local";
    }


}
