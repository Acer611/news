package td.news.biz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import td.news.common.AppType;
import td.news.repository.device.OnlineDeviceRepository;
import td.news.repository.message.MessageRepository;
import td.news.repository.message.dbo.DboOnlineDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static td.news.repository.pushset.PushSettingRepository.allTags;
import static td.news.repository.pushset.PushSettingRepository.defaultTags;


/**
 * @author sanlion do
 */
@Slf4j
@Service
public class PushSettingService {

    @Autowired private MessageRepository messageRepository;
    @Autowired private OnlineDeviceRepository onlineDeviceRepository;


    public void pushset(long uid, AppType appType,
                        int hit, int ssq, int dlt, int fc3d, int pl3, int goal, int master,
                        int live) {
        // 标签整理
        List<String> tags_add = new ArrayList<>();
        if (hit == 1) {
            tags_add.add(allTags.get(0));
        }
        if (ssq == 1) {
            tags_add.add(allTags.get(1));
        }
        if (dlt == 1) {
            tags_add.add(allTags.get(2));
        }
        if (fc3d == 1) {
            tags_add.add(allTags.get(3));
        }
        if (pl3 == 1) {
            tags_add.add(allTags.get(4));
        }
        if (goal == 1) {
            tags_add.add(allTags.get(5));
        }
        if (master == 1) {
            tags_add.add(allTags.get(6));
        }
        if (live == 1) {
            tags_add.add(allTags.get(7));
        }
        // 设置标签
        messageRepository.updatePushTag(uid, tags_add, appType);
    }

    public List<String> listTags(long uid) {
        DboOnlineDevice device = onlineDeviceRepository.device(uid);
        if (Objects.isNull(device)) {
            return defaultTags;
        }
        return device.getPushTag();
    }
}
