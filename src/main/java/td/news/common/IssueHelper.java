package td.news.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by sanlion on 2017/5/25.
 */
public class IssueHelper {



    public static int getDayOfYearByIssue(String issueName){
        LocalDate day = LocalDate.parse(issueName, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return day.getDayOfYear();
    }


}
