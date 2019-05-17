package td.news.repository.message.dbo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sanlion do
 */
@Data
public class CacheNotification_Multi extends CacheNotification_Base {

    List<Long> target = new ArrayList<>();
}
