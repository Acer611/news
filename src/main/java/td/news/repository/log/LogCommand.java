package td.news.repository.log;

import lombok.Data;

import java.util.Date;

@Data
public class LogCommand {
    String level;
    String ip;
    String subject;
    String content;
    String appName;
    Date dtNow = new Date();
    String hostName;
    String ext;
}
