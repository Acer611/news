package td.news.common;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author sanlion do
 */
public class JSONMapper {

    private static final Gson gson = new GsonBuilder().create();

    public  static  String toJson(Object src){
        return gson.toJson(src);
    }

    public static  <T> T fromJson(String src, Class<T> classOfT){
        if (Strings.isNullOrEmpty(src)) {
            return null;
        }
        return gson.fromJson(src, classOfT);
    }
}
