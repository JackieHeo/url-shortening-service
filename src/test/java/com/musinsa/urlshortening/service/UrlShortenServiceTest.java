package com.musinsa.urlshortening.service;

import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import com.musinsa.urlshortening.model.vo.UrlShortenInfo;
import com.musinsa.urlshortening.repository.UrlShortenCacheRepository;
import com.musinsa.urlshortening.repository.UrlShortenRepository;
import com.musinsa.urlshortening.util.Base62;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.util.Strings;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
class UrlShortenServiceTest {

    UrlShortenCacheRepository urlShortenCacheRepository = mock(UrlShortenCacheRepository.class);
    UrlShortenRepository urlShortenRepository = mock(UrlShortenRepository.class);

    UrlShortenService urlShortenService = new UrlShortenService(urlShortenCacheRepository, urlShortenRepository);

    @Test
    @DisplayName("성공 단축 url 생성")
    void set() {

        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening";
        UrlShortenEntity shortenEntity = UrlShortenEntity.builder()
                .id(1L)
                .url(url)
                .count(0L)
                .build();

        when(urlShortenRepository.findByUrl(url)).thenReturn(Optional.empty());
        when(urlShortenRepository.save(any())).thenReturn(shortenEntity);

        //when
        val actual = urlShortenService.createShortenUrl(url);

        //then
        assertNotNull(actual);
        assertEquals(shortenEntity.getId(), Base62.decode(actual));
        verify(urlShortenCacheRepository, times(1)).set(any());
    }

    @Test
    @DisplayName("성공 단축 url 이미 존재 시")
    void setIfExists() {

        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening";
        UrlShortenEntity shortenEntity = UrlShortenEntity.builder()
                .id(1L)
                .url(url)
                .shortenUrl("B")
                .count(0L)
                .build();

        when(urlShortenRepository.findByUrl(url)).thenReturn(Optional.of(shortenEntity));

        //when
        val actual = urlShortenService.createShortenUrl(url);

        //then
        assertNotNull(actual);
        assertEquals(shortenEntity.getId(), Base62.decode(actual));
        verify(urlShortenRepository, never()).save(any());
        verify(urlShortenCacheRepository, never()).set(any());
    }

    @Test
    @DisplayName("성공 단축 url 조회 - 캐시에 정보 있을 경우")
    void getForCache() {

        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening";
        String shortenUrl = "B";

        when(urlShortenCacheRepository.getUrl(shortenUrl)).thenReturn(url);

        //when
        val actual = urlShortenService.getUrl(shortenUrl);

        //then
        assertNotNull(actual);
        assertEquals(url, actual);
        verify(urlShortenCacheRepository, times(1)).getUrl(any());
        verify(urlShortenRepository, never()).findById(any());
    }

    @Test
    @DisplayName("성공 단축 url 조회 - 캐시에 정보 없을 경우")
    void getForDB() {

        //given
        String url = "https://en.wikipedia.org/wiki/URL_shortening";
        String shortenUrl = "B";
        UrlShortenEntity shortenEntity = UrlShortenEntity.builder()
                .id(1L)
                .url(url)
                .shortenUrl("B")
                .count(0L)
                .build();

        when(urlShortenCacheRepository.getUrl(shortenUrl)).thenReturn(Strings.EMPTY);
        when(urlShortenRepository.findById(any())).thenReturn(Optional.of(shortenEntity));

        //when
        val actual = urlShortenService.getUrl(shortenUrl);

        //then
        assertNotNull(actual);
        assertEquals(url, actual);
        verify(urlShortenCacheRepository, times(1)).getUrl(any());
        verify(urlShortenRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("성공 데이터 싱크 - 데이터 갱신")
    void sync() {

        //given
        UrlShortenEntity shortenEntity = UrlShortenEntity.builder()
                .id(1L)
                .url("https://en.wikipedia.org/wiki/URL_shortening")
                .shortenUrl("B")
                .count(0L)
                .build();

        UrlShortenInfo urlShortenInfo = new UrlShortenInfo();
        urlShortenInfo.setId(1L);
        urlShortenInfo.setUrl("https://en.wikipedia.org/wiki/URL_shortening");
        urlShortenInfo.setShortenUrl("B");
        urlShortenInfo.setCount(17L);

        when(urlShortenRepository.findAll()).thenReturn(Lists.newArrayList(shortenEntity));
        when(urlShortenCacheRepository.get(any())).thenReturn(urlShortenInfo);

        //when
        urlShortenService.syncCount();

        //then
        verify(urlShortenRepository, times(1)).findAll();
        verify(urlShortenCacheRepository, times(1)).get(any());
        verify(urlShortenRepository, times(1)).save(any());
    }

}