# 코로나 줄서기 - 입장 인원 카운팅 서비스

코로나19 감염 확산을 막기 위한 사회적 거리두기 지침의 일환으로 체육 시설, 음식점, 각종 이벤트 장소는 현재 인원 제한이 적용되어 있습니다. 그러나 일부 체육관을 포함한 공공시설은 이러한 인원 제한 정보와 시설 이용 현황을 실시간으로 대중에 공유할 수 있는 인프라가 갖춰지지 않았고, 이에 방문객이 시설에 필요에 따라 직접 연락을 취해 확인하는 등 불편이 따르는 상황입니다.

이 프로젝트는 이러한 불편을 조금이나마 해소하기 위한 목적으로 시작되었습니다. 코로나 줄서기 서비스는 쉽게 방문객들이 공공시설 정보를 열람하여 현재 진행 중인 이벤트와 입장 정원, 현재 입장 인원 정보를 확인하고 방문 여부를 결정할 수 있도록 돕습니다. 장소의 정보는 장소 관리자에 의해 직접 관리됩니다.

이 서비스는 [패스트캠퍼스](https://fastcampus.co.kr/)의 [한 번에 끝내는 Spring 완.전.판 초격차 패키지 Online](https://fastcampus.co.kr/dev_online_spring) 강의의 강의용 프로젝트로 사용되었습니다.

## 개발 환경

* Intellij IDEA Ultimate 2021.1.
* Java 16
* Gradle 7.1.1
* Spring Boot 2.5.3

## 기술 세부 스택

Spring Boot

* Spring Boot Actuator
* Spring Data JPA
* Rest Repositories HAL Explorer
* Validation
* Spring Web
* Thymeleaf
* Spring Security
* H2 Database
* MySQL Driver
* Lombok
* Spring Configuration Processor

그 외

* QueryDSL
* Tailwind CSS
* Heroku

## 데모 페이지

코로나 줄서기는 현재 개발이 진행 중입니다. 서비스 데모 페이지는 아래 링크에서 확인하실 수 있습니다.

* https://fastcampus-getinline-test.herokuapp.com/

## 질문, 건의

프로젝트에 관해 궁금하신 점이나 건의 사항이 있으시다면 아래 항목을 이용해 주세요.

* Issues: 버그 리포트, 제안 사항
* Discussions: 프로젝트와 관련한 논의와 정보
