package td.news.biz.discovery.domain;

import lombok.Data;
import td.news.biz.Mbr.domain.BaseUserInfo;
import td.news.biz.news.domain.News;
import td.news.mapper.TD_CP_News.dbo.DbShareScheme;

import java.util.Date;
import java.util.Objects;

/**
 * @author sanlion do
 */
@Data
public class DiscoveryWrapper {

    Date create;
    News news;
    DbShareScheme scheme;
    BaseUserInfo author;
    long topSort;

    public DiscoveryWrapper(News news) {
        this.news = news;
        this.create = news.getCreate();
    }

    public DiscoveryWrapper(DbShareScheme scheme) {
        this.scheme = scheme;
        this.create = scheme.getCreateTime();
        this.topSort = scheme.getIsTop() == 0 ? 0 : (Objects.isNull(scheme.getTopTime()) ? 1 : scheme.getTopTime().getTime());
    }
}
