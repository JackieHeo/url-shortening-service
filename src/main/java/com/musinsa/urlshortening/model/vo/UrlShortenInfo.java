package com.musinsa.urlshortening.model.vo;

import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlShortenInfo {

    private Long id;

    private String url;

    private String shortenUrl;

    private Long count;

    public static UrlShortenInfo of(UrlShortenEntity entity){
        UrlShortenInfo info = new UrlShortenInfo();
        info.id = entity.getId();
        info.url = entity.getUrl();
        info.shortenUrl = entity.getShortenUrl();
        info.count = entity.getCount();
        return info;
    }
}
