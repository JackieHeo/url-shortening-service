package com.musinsa.urlshortening.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "url_shorten")
@NoArgsConstructor
@AllArgsConstructor
public class UrlShortenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 20)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "shorten_url")
    private String shortenUrl;

    @Column(nullable = false, length = 20)
    private Long count;
}