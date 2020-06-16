package com.musinsa.urlshortening.config;

import lombok.Data;

import java.util.Properties;

@Data
public class JpaProperty {
    private Properties properties = new Properties();
}
