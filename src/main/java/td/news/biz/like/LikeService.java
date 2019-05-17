package td.news.biz.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.repository.like.LikeRepository;

/**
 * @author sanlion do
 */
@Service
public class LikeService {

    @Autowired private LikeRepository likeRepository;


    /**
     * 校验是否点赞过
     *
     * @param target
     * @return
     */
    public boolean hasLiked(int type, long target, long uid) {
        if (uid == 0) {
            return false;
        }
        return likeRepository.hasLike(type, target, uid);
    }
}
