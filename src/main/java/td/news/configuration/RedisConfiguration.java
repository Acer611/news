package td.news.configuration;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * Created by sanlion on 2017/6/1.
 */
@Configuration
public class RedisConfiguration {

    @Bean
    @Qualifier("redis6399Template")
    public StringRedisTemplate redis6399Template(
            @Value("${config.redis.user.host}") String host,
            @Value("${config.redis.user.port}") int port,
            @Value("${config.redis.user.password}") String password) {
        int index = 0;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }

    @Bean
    @Qualifier("redis6399db1Template")
    public StringRedisTemplate redis6399db1Template(
            @Value("${config.redis.master.host}") String host,
            @Value("${config.redis.master.port}") int port,
            @Value("${config.redis.master.password}") String password) {
        int index = 1;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }

    @Bean
    @Qualifier("redis6399db2Template")
    public StringRedisTemplate redis6399db2Template(
            @Value("${config.redis.master.host}") String host,
            @Value("${config.redis.master.port}") int port,
            @Value("${config.redis.master.password}") String password) {
        int index = 2;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }

    @Bean
    @Qualifier("redis6399db6Template")
    public StringRedisTemplate redis6399db6Template(
            @Value("${config.redis.hd.host}") String host,
            @Value("${config.redis.hd.port}") int port,
            @Value("${config.redis.hd.password}") String password) {
        int index = 6;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }

    @Bean
    @Qualifier("redis6399db7Template")
    public StringRedisTemplate redis6399db7Template(
            @Value("${config.redis.master.host}") String host,
            @Value("${config.redis.master.port}") int port,
            @Value("${config.redis.master.password}") String password) {
        int index = 7;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }



    @Bean
    @Qualifier("tokenOpt")
    public StringRedisTemplate tokenOpt(
            @Value("${config.redis.token.host}") String host,
            @Value("${config.redis.token.port}") int port,
            @Value("${config.redis.token.password}") String password) {
        int index = 0;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }


    @Bean
    @Qualifier("redis124db1Template")
    public StringRedisTemplate redis124db1Template(
            @Value("${config.redis.news.host}") String host,
            @Value("${config.redis.news.port}") int port,
            @Value("${config.redis.news.password}") String password) {
        int index = 1;
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }

    public LettuceConnectionFactory connectionFactory(
            String host, int port, String password, int index) {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        if (!Strings.isNullOrEmpty(password)) {
            config.setPassword(RedisPassword.of(password));
        }
        if (index != 0) {
            config.setDatabase(index);
        }
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(config);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceConnectionFactory;
    }

    @Bean
    @Qualifier("bizCache")
    public RedisTemplate bizCache(
            @Value("${config.redis.cache.host}") String host,
            @Value("${config.redis.cache.port}") int port,
            @Value("${config.redis.cache.password}") String password) {
        int index = 7;
        RedisTemplate temple = new RedisTemplate();
        temple.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        temple.setConnectionFactory(
                connectionFactory(host, port, password, index));
        return temple;
    }
}
