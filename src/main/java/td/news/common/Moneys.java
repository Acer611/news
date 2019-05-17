package td.news.common;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

/**
 * @author sanlion do
 */
public class Moneys {

    private static final MonetaryAmountFormat format;

    static {
        format = MonetaryFormats.getAmountFormat(AmountFormatQueryBuilder.of(Locale.CHINESE)
                .set(CurrencyStyle.NAME).set("pattern", "#,##0.00").build());
    }

    private static Money money(double money) {
        return Money.of(money, "CNY");
    }

    /**
     * 金币格式化
     *
     * @param money
     * @return
     */
    public static String format(double money) {
        return format.format(money(money));
    }
}
