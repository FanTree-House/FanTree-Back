# FanTree-House
![image (6)](https://github.com/user-attachments/assets/20afa277-ccb4-47fe-b094-f24d82640f12)

## 배포 주소
| [백엔드 배포 주소]() | [프론트 배포 주소]() |

## 👩‍💻 팀원
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/silicao3o"><img src="https://github.com/user-attachments/assets/dd9305d6-f1df-4303-a119-cbc3c852cf91" width="100px;" alt=""/><br /><sub><b> 팀장 : 이시영 </b></sub></a><br /></td>
      <td align="center"><a href="gaeun7"><img src="https://github.com/user-attachments/assets/0c263f93-444c-4beb-bb45-6563bf972e9f" width="100px;" alt=""/><br /><sub><b> 부팀장 : 김가은 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/MonGrony"><img src= width="100px;" alt=""/><br /><sub><b> 팀원 : 김나영 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/kwj0605"><img src="https://github.com/user-attachments/assets/f45111c2-d572-4f41-a0ad-009da8b37cb9" width="100px;" alt=""/><br /><sub><b> 팀원 : 김우진 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/Wooseok1213"><img src="" width="100px;" alt=""/><br /><sub><b> 팀원 : 김우석 </b></sub></a><br /></td>
    </tr>
  </tbody>
</table>


---
## 프로젝트 소개

# **FANTREE HOUSE**

### 🌟 **프로젝트 시작 동기** 🌟

JD 직무분석 및 기획 아이디어
- 대용량 트래픽 관리 경험에 대한 질문 빈번
- 기존 학습한 기능들을 K-POP과 결합한 서비스 기획 필요성 인식
- 핵심 문화산업인 K-POP을 활용한 서비스 개발 아이디어 제안

### **🔎 프로젝트 소개**

팬과 아티스트가 함께 소통하고, 구독을 통한 개인화된 서비스를 제공하여 더욱 가까운 관계를 형성할 수 있는 플랫폼

### **📝 프로젝트 목표**

- 실 사용자 / 휴면 계정 / 블랙리스트에 관한 관리 기능 구현
- 유저 권한에 따른 사용할 수 있는 기능 제한

### 🌟 **주요 기능**
- 그룹 페이지
    - 그룹 피드 작성, 수정, 삭제 (아티스트만)
    - 그룹 피드 조회 (모든 사용자)
    - 그룹 피드 댓글 작성 및 좋아요 (해당 아티스트 그룹 구독한 팬만)
    - 그룹 소개 조회 (모든 사용자)
    - 공지사항 작성, 수정, 삭제 (엔터테인먼트)
    - 공지사항 조회  (모든 사용자)
    - 구독 및 구독 취소 (모든 사용자)
- 유저 커뮤니티
    - 피드 작성(아티스트 그룹 구독한 팬들끼리) CRUD
    - 댓글 CRUD
    - 좋아요
- 유저 마이페이지
  - 내가 좋아요 한 게시물 보기 
  - 구독한 아티스트 목록 
  - 공지사항 및 스케쥴 보기
- 블랙리스트 생성 및 조회, 상태 변경 가능
- 휴면 계정 로직

---
### Stacks 


* <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
* <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
* <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
* <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
* <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
* <img src="https://img.shields.io/badge/Flyway-cc0200?style=for-the-badge&logo=Flyway&logoColor=white">
* <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black">

* <img  src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
* <img  src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">


---
### 실행환경
```
JDK 17
SpringBoot 3.3
```

---
## 아키텍쳐
![image](https://github.com/user-attachments/assets/a91f074d-e840-4e7f-a27e-994b67952c39)

![image](https://github.com/user-attachments/assets/506c175c-02c8-4229-8750-5c22c618b74a)

---
## API 명세서
[API 명세서](https://www.notion.so/teamsparta/8726a50848b84392992f4c9d7281cbe4?v=f346494e067c4471ab4e70997ccc8a25)
  
---
## ERD
![image](https://github.com/user-attachments/assets/2dee9101-ed74-4456-bb42-a8dc33a49193)

---
# ⚖️기술적 의사결정
<div markdown="1">
  
|                                                   도구/기술                                                    |선택이유|
|:----------------------------------------------------------------------------------------------------------:|---|
|MySQL|지금까지 학습한 데이터베이스라 새로운 데이터베이스로 넘어가면 러닝 커브가 커질 것을 우려하였고, 특히 읽기 작업에 최적화되어 대량의 데이터를 다루는 웹 애플리케이션에서 빠른 응답 속도를 유지할 수 있기 때문에 MySQL을 선택 |
|Java 17 |Spring Boot 3.0부터 지원되며, 장기 지원(LTS) 버전으로 안정성과 긴 지원 기간을 제공하기 때문에 선택 |
| Redis| 메일 인증에 필요한 인증 데이터를 잠깐 사용하기 위해 Redis를 캐시에 저장하여, 효율적으로 관리하고 데이터베이스 부하를 줄이기 위해 선택|
|Docker |애플리케이션을 컨테이너화하여 환경에 구애받지 않고 일관되게 배포할 수 있어, 개발과 운영 간의 불일치를 줄이고 효율성을 높이기 위해 선택 |
| EC2|필요에 따라 서버 리소스를 쉽게 조정할 수 있기 때문에 배포 및 공유DB용도로 사용 |
|S3|높은 내구성과 가용성을 제공하여 대량의 사진 데이터를 저장하기 위해 사용|
|Flyway|Batch를 사용함에 있어서 메타 테이블이 필요하다는 것을 알게 되었고, 해당 테이블을 데이터베이스 형상관리 툴인 Flyway를 사용하면서 메타 테이블의 관리를 분리 시킴|
|Spring Batch|휴면 계정으로 전환을 할 때 관리자가 하나하나 바꿀 수 없기 때문에 계정 상태를 프로그램 내부에서 자동적으로 바꿔주는 시스템이 필요했기 때문에 사용|

</div>
