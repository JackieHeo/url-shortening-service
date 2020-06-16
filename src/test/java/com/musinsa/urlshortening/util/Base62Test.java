package com.musinsa.urlshortening.util;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class Base62Test {

    @Test
    void encode(){
        //given
        val shortUrl = "aaCx";

        val id = Base62.decode(shortUrl);
        //when
        val actual = Base62.encode(id);

        //then
        assertEquals(shortUrl, actual);
    }
}