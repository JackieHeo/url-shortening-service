package com.musinsa.urlshortening.controller;

import com.musinsa.urlshortening.service.UrlShortenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlShortenBatchController {

    private final UrlShortenService urlShortenService;

    @ResponseBody
    //@PostMapping(value = "/batch/url-shorten/sync")
    @GetMapping(value = "/batch/url-shorten/sync")
    public void syncCount() {
        urlShortenService.syncCount();
    }
}
