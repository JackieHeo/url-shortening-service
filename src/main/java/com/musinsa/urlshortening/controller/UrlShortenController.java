package com.musinsa.urlshortening.controller;

import com.musinsa.urlshortening.model.request.UrlShortenRequest;
import com.musinsa.urlshortening.model.response.ServiceResponse;
import com.musinsa.urlshortening.service.UrlShortenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UrlShortenController {
    private final UrlShortenService urlShortenService;
    private final String DEFAULT_DOMAIN = "http://localhost:8080/";

    @ResponseBody
    @PostMapping(value = "/url-shorten")
    public ServiceResponse<String> getShortenUrl(@Valid @RequestBody UrlShortenRequest request) {
        return new ServiceResponse<>(DEFAULT_DOMAIN + urlShortenService.createShortenUrl(request.getUrl().trim()));
    }
}
