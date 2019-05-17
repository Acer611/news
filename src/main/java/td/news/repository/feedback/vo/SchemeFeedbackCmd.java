package td.news.repository.feedback.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sanlion do
 */
@Data
@AllArgsConstructor
public class SchemeFeedbackCmd {

    long uid;
    long scheme;
    String content;
}
