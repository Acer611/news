package td.news.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by PC on 2017/3/23.
 */
public class Times {
    public static final String __ = "M月dd日 HH:mm";
    public static final String MMddHHmm = "MM-dd HH:mm";
    public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMdd = "yyyy-MM-dd";

    /**
     * 格式化 <- 默认值，空
     *
     * @param date    时间
     * @param pattern 模式
     * @return
     */
    public static String format(Date date, String pattern) {
        return format(date, pattern, "");
    }

    /**
     * 格式化
     *
     * @param date    时间
     * @param pattern 模式
     * @param ifNull  默认值
     * @return
     */
    public static String format(Date date, String pattern, String ifNull) {
        if (Objects.isNull(date)) {
            return ifNull;
        }
        try {
            return
                    LocalDateTime
                            .ofInstant(date.toInstant(), ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            return ifNull;
        }
    }

    public static Date parse(String date, String pattern) {
        return Date.from(
                LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern)).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isToday(Date date) {
        return !Objects.isNull(date) && LocalDate.now().isEqual(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate());
    }

    /**
     * 枚举最近7天的day
     *
     * @return
     */
    public static List<Integer> listLatest7days() {
        LocalDate now = LocalDate.now();
        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(now.minusDays(i).getDayOfMonth());
        }
        return days;
    }

    public static int state(Date begin, Date end) {
        if (Objects.isNull(begin) || Objects.isNull(end)) {
            return 3;
        }
        Date now = new Date();
        if (now.before(begin)) {
            return 1;
        }
        if (now.after(end)) {
            return 3;
        }
        return 2;
    }

    public static Date delayToWorkingTime(Date now) {
        now = Objects.isNull(now) ? new Date() : now;
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now.toInstant(), ZoneId.systemDefault());
        LocalDateTime target = null;
        if (localDateTime.getHour() >= 23) {
            // 延到第二天8点
            target = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, localDateTime.getMinute()));
        }
        if (localDateTime.getHour() <= 8) {
            // 延时到8点之后
            target = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(8, localDateTime.getMinute()));
        }
        return Objects.isNull(target) ? now : Date.from(target.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static double getScore(Date time){
        return  (double) time.getTime() / 10000000000000L;
    }
}
