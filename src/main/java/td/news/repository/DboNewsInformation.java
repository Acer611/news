package td.news.repository;

import lombok.Data;

import java.util.Date;

@Data
public class DboNewsInformation {
    private long id;
    private String title;
    private String icon;
    private String link;
    private String content;
    private String author;
    private String origin;
    private Date update = new Date();
    private Date begin;
    private Date end;
}
