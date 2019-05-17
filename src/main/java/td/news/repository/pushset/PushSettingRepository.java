package td.news.repository.pushset;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author sanlion do
 */
@Service
public class PushSettingRepository {

    public static final List<String> allTags = Arrays.asList(
            "drawn", "open_ssq", "open_dlt", "open_3d", "open_pl3", "football", "masterplan","live");
    public static final List<String> defaultTags = Arrays.asList(
            "drawn", "open_ssq", "open_dlt");
}
