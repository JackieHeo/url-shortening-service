# url-shortening-service

## 설명
  - 단축 url 생성
    - 기본 정보를 테이블에 입력 후 테이블의 기본키 값을 base62 인코딩하여 단축 url을 생성했습니다.
  
  - 동일한 URL에 대한 요청 수 정보를 가져야 한다
    - 요청 건을 매번 db 처리 하는 것보다 redis 캐시를 쓰는 것이 효율적이라 판단했습니다.
    - 배치를 통해 레디스에 있는 정보와 DB 정보는 동기화 하는 형태로 구현했습니다.
 
## Spec
  - Spring Boot 2.1.1
    - Java 1.8
    - Gradle
    - MYSQL
    - REDIS
  - Test
    - JUnit5
    
## Run
  - 설정된 값
    - REDIS 
        - host : 127.0.0.1
        - port : 6379
    - MYSQL
        - host : 127.0.0.1
        - port : 3306
        - user name : root
        - password : 1234
        - 스키마 : common
        - 테이블 : 아래의 ddl 문을 수행
    - 변경이 필요할 경우 application.yml 파일에서 수정 필요 합니다(main, test 경로). 
    
  - Project 경로에서 gradle bootRun 입력(Linux의 경우 실행에 실패할시 sudo gradle bootRun으로 실행)
  
  - host(예 http://localhost:8080/) 접속 (요청 값에 따라 등록 화면 또는 리다이렉트 됩니다)
  
## DDL
CREATE TABLE `url_shorten` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(200) DEFAULT NULL,
  `shorten_url` char(8) DEFAULT NULL,
  `count` bigint(20) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

## Test
  - 테스트 코드는 service, repository 단위로 구현했습니다.