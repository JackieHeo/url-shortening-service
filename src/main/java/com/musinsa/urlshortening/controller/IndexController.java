package com.musinsa.urlshortening.controller;

import com.musinsa.urlshortening.service.UrlShortenService;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UrlShortenService urlShortenService;

    @GetMapping(value = "/")
    public String main() {
        return "register";
    }

    @GetMapping(value = "/{shortenUrl}")
    public String index(@PathVariable String shortenUrl){
        if(StringUtil.isNullOrEmpty(shortenUrl)){
            return "register";
        }

        String url = urlShortenService.getUrl(shortenUrl);
        return StringUtil.isNullOrEmpty(url) ? "register" : "redirect:" + url;
    }
}
