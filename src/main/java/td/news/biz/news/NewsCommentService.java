package td.news.biz.news;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.biz.Mbr.MbrService;
import td.news.biz.Mbr.domain.BaseUserInfo;
import td.news.biz.news.domain.NewsComment;
import td.news.biz.news.domain.SimpleComment;
import td.news.mapper.TD_CP_News.DbComment;
import td.news.mapper.TD_CP_News.DbSimpleComment;
import td.news.mapper.TD_CP_News.NewsCommentMapper;
import td.news.repository.comment.CommentRepository;
import td.news.repository.spider.RandomUserRepository;
import td.news.repository.spider.dbo.RandomUser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sanlion do
 */
@Service
public class NewsCommentService {

    @Autowired private NewsCommentMapper newsCommentMapper;
    @Autowired private MbrService mbrService;
    @Autowired private RandomUserRepository randomUserRepository;
    @Autowired private CommentRepository commentRepository;

    public List<NewsComment> listAll(long uid, int page, int pageSize) {
        List<DbComment> value
                = newsCommentMapper.listAll(uid, new RowBounds((page - 1) * pageSize, pageSize));
        if (Objects.isNull(value) || value.isEmpty()) {
            return Collections.emptyList();
        }
        return value.stream().map(
                NewsComment::new).collect(Collectors.toList());
    }


    public void comment(long uid, long newsid, String content) {
        /*
        发帖默认为不审核，但是发现里面有联系方式的，直接拒绝，且此人禁言
         */
        BaseUserInfo user = mbrService.getById(uid);
        String nick = user.showName();
        String avatar = user.getAvatar();
        String username = user.getName();
        int auditState = 0;
        RandomUser randomUser = randomUserRepository.getRandomUser(uid, nick, avatar);
        if (Objects.nonNull(randomUser)) {
            avatar = randomUser.getAvatar();
            nick = randomUser.getNick();
        }
//        if (diRepository.checkAuthorInBlacklist(uid)) {
//            auditState = 2;
//        } else {
//            if (NicknameConverter.containsLink(content)) {
//                auditState = 2;
//                diRepository.gag(uid);
//            } else if (gagRepository.containsGagWords(content)) {
//                auditState = 2;
//            } else if (!mbrService.checkCommentMoneyBuy(uid)) {
//                auditState = 0;
//            }
//        }

        if (0 == auditState) {
            if (commentRepository.repeat(content, uid)) {
                auditState = 2;
            }
        }


        newsCommentMapper.comment(uid, newsid, content, nick, avatar, username, auditState);
        commentRepository.logRepeat(uid, content);
    }

    public void delete(long uid, long id) {
        newsCommentMapper.delete(uid, id);
    }

    public List<SimpleComment> listByNews(long uid, long newsid, int page, int pageSize) {
        List<DbSimpleComment> value
                = newsCommentMapper.listByNews(uid, newsid, new RowBounds((page - 1) * pageSize, pageSize));
        if (Objects.isNull(value) || value.isEmpty()) {
            return Collections.emptyList();
        }
        return value.stream().map(it -> new SimpleComment(it)).collect(Collectors.toList());
    }

    public List<SimpleComment> listByNewsOnlyBySelf(long uid, long newsid, int page, int pageSize) {
        List<DbSimpleComment> value
                = newsCommentMapper.listByNewsOnlyBySelf(uid, newsid, new RowBounds((page - 1) * pageSize, pageSize));
        if (Objects.isNull(value) || value.isEmpty()) {
            return Collections.emptyList();
        }
        return value.stream().map(it -> new SimpleComment(it)).collect(Collectors.toList());
    }
}
