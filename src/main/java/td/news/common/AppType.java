package td.news.common;

import lombok.Getter;

public enum AppType {

    /**
     * 彩票（之前所有的包）
     */
    Caipiao(0),

    /**
     * 彩帝联盟
     */
    Caidi(1);

    @Getter private int code;

    AppType(int code) {
        this.code = code;
    }
}
