package com.musinsa.urlshortening.service;

import com.musinsa.urlshortening.exception.ApplicationException;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(value = "local")
public class UrlShortenServiceIntegrationTest {
    @Autowired
    UrlShortenService urlShortenService;

    @Test
    @DisplayName("성공 - url 등록 및 조회")
    void set(){
        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening123";
        //when
        val actual = urlShortenService.createShortenUrl(url);
        val actualUrl = urlShortenService.getUrl(actual);
        //then
        assertNotEquals("", actual);
        assertEquals(url, actualUrl);
    }

    @Test
    @DisplayName("성공 - 존재하지 않는 단축 url 조회")
    void get(){
        //given
        String shortenUrl = "XXXXX";
        //when
        val actual = urlShortenService.getUrl(shortenUrl);
        //then
        assertNull(actual);
    }
}
