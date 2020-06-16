package com.musinsa.urlshortening.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
public class UrlShortenRequest {

    @URL
    @NotEmpty
    private String url;
}
