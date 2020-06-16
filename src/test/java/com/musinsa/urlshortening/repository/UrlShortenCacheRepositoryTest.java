package com.musinsa.urlshortening.repository;

import com.musinsa.urlshortening.config.RedisConfig;
import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RedisConfig.class, UrlShortenCacheRepository.class} )
@ActiveProfiles(value = "local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UrlShortenCacheRepositoryTest {

    @Autowired
    UrlShortenCacheRepository urlShortenCacheRepository;

    @Test
    @DisplayName("성공 저장 및 조회")
    @Order(1)
    void set() {

        //given
        val url = "https://en.wikipedia.org/wiki/URL_shortening";
        val shortenUrl = "KK";
        UrlShortenEntity shortenEntity = UrlShortenEntity.builder()
                .id(1L)
                .url(url)
                .shortenUrl(shortenUrl)
                .count(0L)
                .build();
        urlShortenCacheRepository.set(shortenEntity);
        //when
        val actual = urlShortenCacheRepository.getUrl(shortenUrl);

        //then
        assertEquals(url, actual);
    }

    @Test
    @DisplayName("성공 단축 url 조회 데이터 없을 경우")
    void getIfNotExist() {

        //given
        val shortenUrl = "I";

        //when
        val actual = urlShortenCacheRepository.getUrl(shortenUrl);

        //then
        assertEquals(Strings.EMPTY, actual);
    }

    @Test
    @DisplayName("성공 조회")
    void get() {

        //given
        val shortenUrl = "B";

        //when
        val actual = urlShortenCacheRepository.get(shortenUrl);

        //then
        assertEquals(shortenUrl, actual.getShortenUrl());
    }
}