package td.news.repository.di.dbo;

import lombok.Data;

import java.util.Date;

/**
 * @author sanlion do
 */
@Data
public class T_MasterRegionConfig {

    long MasterID;
    long RegionID;
    String RegionName;
    Date CreateTime;
}
