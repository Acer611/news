package td.news.biz.news.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import td.news.repository.DboNewsCategory;

/**
 * @author sanlion do
 */
@Data
@AllArgsConstructor
public class NewsCategory {

    private String code;
    private String name;

    public NewsCategory(DboNewsCategory value) {

        this.code = value.getCode();
        this.name = value.getName();
    }
}
