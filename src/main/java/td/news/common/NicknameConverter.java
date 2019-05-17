package td.news.common;

import com.google.common.base.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sanlion do
 */
public class NicknameConverter {

    private static final Pattern PATTERN_PHONE = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$");
    private static final Pattern PATTERN_4_LETTER = Pattern.compile("\\d+(\\d{4})");
    private static final Pattern PATTERN_CLEAR = Pattern.compile("[^0-9a-zA-Z\\u4e00-\\u9fa5]");


    public static String convert(String nickname) {
        if (Strings.isNullOrEmpty(nickname)) {
            return nickname;
        }
        if (isMobile(nickname)) {
            return mobileConvert(nickname);
        }
        return NumberConvert(nickname);
    }

    private static String mobileConvert(String nickname) {
        return nickname.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    private static String NumberConvert(String nickname) {
        Matcher matcher = PATTERN_4_LETTER.matcher(nickname);
        if (matcher.find()) {
            return matcher.replaceAll("****$1");
        }
        return nickname;
    }

    public static boolean isMobile(String nickname) {
        Matcher matcher = PATTERN_PHONE.matcher(nickname);
        return matcher.matches();
    }

    final static List<String> ignore = Arrays.asList("11111", "22222", "33333", "44444", "55555", "66666", "77777", "88888", "99999");
    final static String qqPattern = "[1-9][0-9]{5,14}";
    final static String qqOrMobilePatter = "1[3-9]\\d{9}";
    final static String wxPattern = "[a-zA-Z0-9][-_a-zA-Z0-9]{5,25}";

    public static boolean containsLink(String text) {
        text = clear(text);
        boolean match = false;
        String pattern = "";
        Matcher qqMatcher = Pattern.compile(qqPattern).matcher(text);
        if (qqMatcher.find()) {
            match = true;
            pattern = qqMatcher.group();
        }
        Matcher qqOrMobileMatcher = Pattern.compile(qqOrMobilePatter).matcher(text);
        if (qqOrMobileMatcher.find()) {
            match = true;
            pattern = qqOrMobileMatcher.group();
        }
        Matcher wxMatcher = Pattern.compile(wxPattern).matcher(text);
        if (wxMatcher.find()) {
            match = true;
            pattern = wxMatcher.group();
        }
        if (match && !Strings.isNullOrEmpty(pattern)) {
            String finalPattern = pattern;
            return ignore.stream().noneMatch(it -> finalPattern.contains(it));
        }
        return false;
    }

    public static String clear(String origin){
        Matcher m = PATTERN_CLEAR.matcher(origin);
        String clear = m.replaceAll("").trim();
        return clear;
    }


}
