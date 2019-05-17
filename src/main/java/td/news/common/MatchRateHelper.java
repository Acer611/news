package td.news.common;

import com.google.common.base.Strings;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
public class MatchRateHelper {

    /**
     * 计算打出的概率
     *
     * @param a
     * @param b
     * @param c
     * @param x
     * @return
     */
    public static double rate(double a, double b, double c, double x) {
        return (1 / (1 / a + 1 / b + 1 / c)) / x;
    }

    public static List<String> rate(String spf){
        List<String> rates = new ArrayList<>();
        if (!Strings.isNullOrEmpty(spf) && spf.split(",").length > 0) {
            try {
                String[] split = spf.split(",");
                double a= Double.parseDouble(split[0]);
                double b= Double.parseDouble(split[1]);
                double c= Double.parseDouble(split[2]);
                NumberFormat percentInstance = NumberFormat.getPercentInstance();
                rates.add(percentInstance.format(rate(a,b,c,a)));
                rates.add(percentInstance.format(rate(a,b,c,b)));
                rates.add(percentInstance.format(rate(a,b,c,c)));
            }catch (Exception e){}
        }
        return rates;
    }
}
