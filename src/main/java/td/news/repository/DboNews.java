package td.news.repository;

import lombok.Data;

import java.util.Date;

@Data
public class DboNews {

    private long id;
    private String title;
    private String icon;
    private String link;
    private Date update = new Date();
    private Date begin;
    private Date end;
    private Date create = new Date();
    private String origin;
    private String author;
    private int sort;
    private int PushTypeID;
}
