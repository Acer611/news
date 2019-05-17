package td.news.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;

/**
 * Created by sanlion on 2017/5/22.
 */
@EnableCaching
@Configuration
public class CacheConfiguration {

    public static final String cachePushCert = "cache-pushCert";
    public static final String bizCache_1_min = "bizCache_1_min"; // redis公共缓存
    public static final String bizCache_10_min = "bizCache_10_min"; // redis公共缓存
    public static final String bizCache_5_min = "bizCache_5_min"; // redis公共缓存
    public static final String bizCache_30_min = "bizCache_30_min"; // redis公共缓存
    public static final String bizCache_60_min = "bizCache_60_min"; // redis公共缓存


    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                bizCache_1_min(),
                bizCache_5_min(),
                bizCache_10_min(),
                bizCache_30_min(),
                bizCache_60_min()
        ));
        return cacheManager;
    }

    @Autowired
    @Qualifier("bizCache")
    private RedisTemplate bizCacheTemplate;
    private static final String bizCachePrefixKey = "cache:";

    public RedisCache bizCache_1_min() {
        RedisCacheConfiguration redisCacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .prefixKeysWith(bizCachePrefixKey)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCacheManager manager = RedisCacheManager.builder(bizCacheTemplate.getConnectionFactory())
                .cacheDefaults(redisCacheConfiguration).build();
        return (RedisCache) manager.getCache(bizCache_1_min);
    }

    public RedisCache bizCache_5_min() {
        RedisCacheConfiguration redisCacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .prefixKeysWith(bizCachePrefixKey)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCacheManager manager = RedisCacheManager.builder(bizCacheTemplate.getConnectionFactory())
                .cacheDefaults(redisCacheConfiguration).build();
        return (RedisCache) manager.getCache(bizCache_5_min);
    }

    public RedisCache bizCache_10_min() {
        RedisCacheConfiguration redisCacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .prefixKeysWith(bizCachePrefixKey)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCacheManager manager = RedisCacheManager.builder(bizCacheTemplate.getConnectionFactory())
                .cacheDefaults(redisCacheConfiguration).build();
        return (RedisCache) manager.getCache(bizCache_10_min);
    }

    public RedisCache bizCache_30_min() {
        RedisCacheConfiguration redisCacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .prefixKeysWith(bizCachePrefixKey)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCacheManager manager = RedisCacheManager.builder(bizCacheTemplate.getConnectionFactory())
                .cacheDefaults(redisCacheConfiguration).build();
        return (RedisCache) manager.getCache(bizCache_30_min);
    }

    public RedisCache bizCache_60_min() {
        RedisCacheConfiguration redisCacheConfiguration
                = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .prefixKeysWith(bizCachePrefixKey)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        RedisCacheManager manager = RedisCacheManager.builder(bizCacheTemplate.getConnectionFactory())
                .cacheDefaults(redisCacheConfiguration).build();
        return (RedisCache) manager.getCache(bizCache_60_min);
    }


    @Bean
    public KeyGenerator simpleMethodNameKeyGenerator() {
        return (target, method, params) -> {
            if (params.length == 0) {
                return method.getDeclaringClass().getName() + ":" + method.getName();
            }
            if (params.length == 1) {
                Object param = params[0];
                if (param != null && !param.getClass().isArray()) {
                    return method.getDeclaringClass().getName() + ":" + method.getName() + ":" + param;
                }
            }
            return method.getDeclaringClass().getName() + ":" + method.getName() + ":[" + StringUtils.arrayToCommaDelimitedString(params) + "]";
        };
    }


}
