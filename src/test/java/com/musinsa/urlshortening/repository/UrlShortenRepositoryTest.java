package com.musinsa.urlshortening.repository;

import com.musinsa.urlshortening.config.JpaConfig;
import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {JpaConfig.class} )
@ActiveProfiles(value = "local")
class UrlShortenRepositoryTest {

    @Autowired
    UrlShortenRepository urlShortenRepository;

    @Test
    @DisplayName("성공 저장")
    @Transactional
    void set() {

        //given
        val entity = UrlShortenEntity.builder()
                .url("https://en.wikipedia.org/wiki/URL_shortening/insert")
                .shortenUrl("xxa")
                .build();
        //when
        val actual = urlShortenRepository.save(entity);

        //then
        assertEquals(entity, actual);
    }

    @Test
    @DisplayName("성공 조회")
    void get() {
        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening";

        //when
        val actual = urlShortenRepository.findByUrl(url);

        //then
        assertNotNull(actual);
        assertEquals(url, actual.get().getUrl());
    }

    @Test
    @DisplayName("성공 조회 - 데이터 없을 경우")
    void getIfNotExist() {
        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening1231";

        //when
        val actual = urlShortenRepository.findByUrl(url);

        //then
        assertEquals(Optional.empty(), actual);
    }
}