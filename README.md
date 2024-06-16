# 미니 프로젝트: TODO List 서비스

## 스펙
- java 11
- spring boot 2.7.10
- mysql:8.0
- docker compose 2.4


## 실행 가이드
docker compose를 통해 build, running 동시 진행
```shell
docker compose up -d
```

### 주의 사항
- mac m시리즈에서 실행시 `docker-compose.yaml` 파일의 `#platform: linux/amd64 # mac m series` 주석 제거 후 running

## 기본 port
- todo service
  - 8090
- todo db
  - 3306

## swagger API 첨부
- http://localhost:8090/swagger-ui/index.html

