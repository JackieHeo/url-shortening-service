package com.musinsa.urlshortening.service;

import com.musinsa.urlshortening.exception.ApplicationException;
import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import com.musinsa.urlshortening.model.vo.UrlShortenInfo;
import com.musinsa.urlshortening.repository.UrlShortenCacheRepository;
import com.musinsa.urlshortening.repository.UrlShortenRepository;
import com.musinsa.urlshortening.util.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlShortenService {
    private final UrlShortenCacheRepository urlShortenCacheRepository;
    private final UrlShortenRepository urlShortenRepository;

    @Transactional
    public String createShortenUrl(String url){
        // 1. db에 정보 이미 존재 여부 체크 - 존재 한다면 바로 정보 반환
        Optional<UrlShortenEntity> shortenEntityOptional = urlShortenRepository.findByUrl(url);
        if(shortenEntityOptional.isPresent()){
            return shortenEntityOptional.get().getShortenUrl();
        }
        //2. 요청 정보 db 저장
        UrlShortenEntity shortenEntity = urlShortenRepository.save(UrlShortenEntity.builder()
                .url(url)
                .count(0L)
                .build());
        //3. 단축 url 생성 - db id 값을 base62 인코딩
        String shortenUrl = Base62.encode(shortenEntity.getId());
        if(shortenUrl.length() > 9){
            throw new ApplicationException("URL Shortening Key는 8 Character 이내로 생성 되어야 합니다.");
        }
        //4. db entity 정보 단축 url 정보 set and flush
        shortenEntity.setShortenUrl(shortenUrl);
        //5. redis 정보 저장
        urlShortenCacheRepository.set(shortenEntity);
        return shortenUrl;
    }

    public String getUrl(String shortenUrl){
        String url = urlShortenCacheRepository.getUrl(shortenUrl);
        if(Strings.isNotEmpty(url)){
            return url;
        }
        return urlShortenRepository.findById(Base62.decode(shortenUrl))
                .orElseGet(UrlShortenEntity::new)
                .getUrl();
    }

    @Transactional
    public void syncCount(){
        //1. db 모든 정보 조회
        List<UrlShortenEntity> urlShortenEntities = urlShortenRepository.findAll();

        //2. 레디스 카운트 정보 db 동기화
        try{
            urlShortenEntities.forEach(entity -> {

                //레디스 정보 조회
                UrlShortenInfo urlShortenInfo = urlShortenCacheRepository.get(entity.getShortenUrl());

                //카운트 정보 수정
                entity.setCount(urlShortenInfo.getCount());
                urlShortenRepository.save(entity);
            });

        }catch (Exception e){
            log.error("failed to sync count :", e);
        }

    }

}
