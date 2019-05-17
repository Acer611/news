package td.news.biz.Mbr.domain;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.NoArgsConstructor;
import td.news.common.NicknameConverter;
import td.news.repository.mbr.dbo.DbBaseUserInfo;

/**
 * @author sanlion do
 */
@Data
public class BaseUserInfo{

    private long uid;
    private String name;
    private String nickname;
    private String avatar;

    public BaseUserInfo() {
    }

    public BaseUserInfo(DbBaseUserInfo value) {
        this.uid = value.getID();
        this.name = value.getName();
        this.nickname = value.getNickName();
        this.avatar = value.getFaceUrl();
    }
    public String showName(){
        return NicknameConverter.convert(!Strings.isNullOrEmpty(this.nickname) ? this.nickname : this.name);
    }
}
