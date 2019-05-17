package td.news.biz.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.repository.feedback.FeedbackResponse;
import td.news.repository.feedback.vo.SchemeFeedbackCmd;

/**
 * @author sanlion do
 */
@Service
public class FeedbackService {

    @Autowired private FeedbackResponse feedbackResponse;

    public void feedbackByScheme(long uid, long scheme, String content) {
        feedbackResponse.gteCmd_feedbackByScheme(new SchemeFeedbackCmd(uid, scheme, content));
    }
}
