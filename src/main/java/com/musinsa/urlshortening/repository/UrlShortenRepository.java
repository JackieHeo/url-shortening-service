package com.musinsa.urlshortening.repository;

import com.musinsa.urlshortening.model.entity.UrlShortenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenRepository extends JpaRepository<UrlShortenEntity, Long> {
    Optional<UrlShortenEntity> findByUrl(String url);
}
