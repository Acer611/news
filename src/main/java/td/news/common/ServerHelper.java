package td.news.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerHelper {

    public static String ip() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return "java";
        }
        return addr.getHostAddress(); //获取本机ip
    }
}
