package td.news.repository.device.dbo;

import lombok.Data;

/**
 * @author sanlion do
 */
@Data
public class CacheAppVersion {

    long ID;
    int Platform;
    String VersionNo;
    String AppStore;
    int AppCode;
}
