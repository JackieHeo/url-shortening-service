package com.musinsa.urlshortening.repository;

import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import com.musinsa.urlshortening.model.vo.UrlShortenInfo;
import com.musinsa.urlshortening.util.Base62;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UrlShortenCacheRepository {

    private static final String REDIS_KEY_PREFIX = "url-shortening:";
    private final RedisTemplate<String, UrlShortenInfo> redisTemplate;

    private String getKey(String shortenUrl){
        return REDIS_KEY_PREFIX + Base62.decode(shortenUrl);
    }

    public void set(UrlShortenEntity entity){
        redisTemplate.opsForValue()
                .set(getKey(entity.getShortenUrl()), UrlShortenInfo.of(entity));
    }

    public UrlShortenInfo get(String shortenUrl){
        return redisTemplate.opsForValue().get(getKey(shortenUrl));
    }

    public String getUrl(String shortenUrl){
        try{
            String key = getKey(shortenUrl);
            UrlShortenInfo result = redisTemplate.opsForValue().get(key);
            if(Objects.isNull(result)){
                throw new Exception("data not found for cache");
            }
            result.setCount(result.getCount()+1);
            redisTemplate.opsForValue().set(key, result);
            return result.getUrl();
        }catch (Exception e){
            return Strings.EMPTY;
        }
    }
}
