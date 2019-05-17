package td.news.api.api.chart;

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import td.news.common.BizException;
import td.news.repository.chart.ChartRepository;
import td.news.repository.chart.cache.CacheChartUrl;

import java.util.Objects;

/**
 * @author sanlion do
 */
@Slf4j
@Controller
@RequestMapping("/lotteryChart")
@Api(tags = "chart", description = "数字彩图表")
public class LotteryChartRedirect {

    @Autowired private ChartRepository chartRepository;

    @GetMapping
    String chart(
            @RequestParam("lottery") int lottery,
            @RequestParam(required = false, defaultValue = "miss") Type type) {
        CacheChartUrl chartRedirectByLottery = chartRepository.getChartRedirectByLottery(lottery);
        if (Objects.isNull(chartRedirectByLottery)) {
            throw new BizException(17122501, "暂无图表数据");
        }
        String redirect = null;
        if (Type.miss.equals(type)) {
            redirect = chartRedirectByLottery.getMiss();
        }

        if (Type.trend.equals(type)) {
            redirect = chartRedirectByLottery.getTrend();
        }

        if (Strings.isNullOrEmpty(redirect)) {
            throw new BizException(17122501, "暂无图表数据");
        }
        return "redirect:" + redirect;
    }

    public enum Type {
        miss, trend
    }
}
