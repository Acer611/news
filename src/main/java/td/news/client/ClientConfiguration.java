/*
    © sanlion.do 
 */
package td.news.client;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.gson.GsonDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sanlion
 */
@Slf4j
@Configuration
@EnableFeignClients
public class ClientConfiguration {

    @Bean
    public Request.Options options(){
        return new Request.Options(5*1000, 2* 1000);
    }

    @Bean
    public Decoder decoder() {
        return new GsonDecoder();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
//
    @Bean
    public Retryer retryer(){
        return Retryer.NEVER_RETRY;
    }


}
