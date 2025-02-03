# 📅 일정 관리 앱 서버 만들기

## 📝 프로젝트 개요

- **프로젝트 이름**: 일정 관리 앱 서버 만들기

- **프로젝트 기간**: 25.01.23(목) ~ 25.02.04(화)

## 🚀 사용 기술

- Spring Boot

- Spring Data JDBC

- MySQL

## 🛠 개발 환경

- Java: 17.0.14

- 빌드 도구: Gradle 8.11.1

- 프레임워크: Spring Boot 3.3.1

- 데이터베이스: MySQL 8.3.0

### 깃 클론 시

`src/main/resources/application-dev.yml` 을 만들어주셔야 합니다.

```yml
# application-dev.yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: <YOUR_MYSQL_URL>
    username: <YOUR_MYSQL_USERNAME>
    password: <YOUR_MYSQL_PASSWORD>
```

## 📖 API 명세서

 - [필수 기능 API 명세서](https://drive.google.com/file/d/1ZUDtN9jHD7OVdez25yI6DP-017wke2k3/view?usp=drive_link)
 - [도전 기능 API 명세서](https://drive.google.com/file/d/1t5zcbu3MaLt0j1Cv1-4RzQEIgyX_mWxO/view?usp=drive_link)

## 🗂 ERD

### 필수 기능 과제 ERD

![필수 기능 과제 ERD](https://github.com/user-attachments/assets/79d23272-d677-4648-86a0-14b0caa7eb28)

### 도전 기능 과제 ERD

![도전 기능 과제 ERD](https://github.com/user-attachments/assets/c38045d2-f2d2-4d1e-b0af-0dba89a11d2d)
