# netflix_clone(진행중)

# 🎬 Netflix Clone Project (진행 중)

> 영화 스트리밍 서비스 Netflix를 Spring Boot 기반으로 개발 역량을 학습하고 구축하는 개인 토이 프로젝트입니다.

---

## 프로젝트 미리보기

<img width="1354" height="592" alt="image" src="https://github.com/user-attachments/assets/4679ab6e-a3ea-4b8b-8cf5-296bac02f84c" />

<img width="1322" height="523" alt="image" src="https://github.com/user-attachments/assets/2f9e73fa-c207-4991-902e-7796440e3508" />


---

## 💡 프로젝트 목표 및 주요 기능

### 🎯 프로젝트 목표
*   Java, Spring Boot 기반의 웹 애플리케이션 개발 전체 프로세스 이해 및 숙달
*   관계형 데이터베이스(MySQL) 설계 및 MyBatis를 활용한 데이터 관리 능력 향상
*   AJAX를 활용한 비동기 통신 구현 및 사용자 경험 개선
*   문제 해결 능력 및 디버깅 능력 강화

### ✨ 주요 기능 및 현재 진행 상황

| 기능 분류      | 구현된 기능                                     | 진행 상황 | 비고                                            |
|--------------|-----------------------------------------------|-----------|-------------------------------------------------|
| **회원 관리** | 회원가입, 로그인, 아이디 중복 검사, 비밀번호 찾기/수정, 회원 탈퇴 | ✅ 구현 완료 | BCryptPasswordEncoder로 비밀번호 해시화                 |
| **영화 정보**  | 영화 검색, 영화 정보 조회 (제목, 포스터, 상세), 영화 등록/수정/삭제 | ✅ 구현 완료 | 타임리프를 활용한 동적 데이터 표시              |
| **시청 관리**  | 시청 기록 저장 및 조회                       | 🚧 구현 중 |                                                 |
| **리뷰 시스템**| 영화 리뷰 작성, 추천 수 업데이트 | 🚧 구현 중  |       |
| **추천 시스템**| 기본적인 영상 추천 기능                      | 🚧 구현 중  |        |


## 🛠️ 기술 스택 (Tech Stack)

<img src="https://skillicons.dev/icons?i=java,spring,javascript,html,css,mysql,mybatis,thymeleaf" />


## ⚠️ 트러블 슈팅 및 문제 해결

**[사례 1] 스프링 시큐리티 BCryptPasswordEncoder 적용 후 로그인 실패 문제 해결**

문제 상황: 
회원가입 기능을 구현하며 Spring Security에서 제공하는 BCryptPasswordEncoder 빈(Bean)을 등록하고, 서비스 로직에서 이를 호출하여 비밀번호를 해시화하여 데이터베이스에 INSERT하도록 설정했습니다. 
DB에 저장된 비밀번호 값이 정상적으로 해시화되어 있음을 확인했지만, 회원가입 시 사용한 아이디와 비밀번호로 로그인 시도 시 지속적으로 로그인에 실패하는 문제가 발생했습니다.

원인 분석: 초기 로그인 로직은 MyBatis Mapper에서 아이디(#{userId})와 함께 사용자가 입력한 비밀번호(#{userPw})를 쿼리에 직접 넣어 DB에 저장된 값과 비교하는 방식이었습니다.

-- 기존 로그인 쿼리
SELECT * FROM USER_TABLE WHERE user_id = #{userId} AND user_pw = #{userPw}
그러나 BCryptPasswordEncoder로 해시화된 비밀번호는 평문(사용자가 입력한 비밀번호)과 직접적으로 비교할 수 없습니다. 
사용자가 입력한 평문 비밀번호가 #{userPw}로 넘어와도, DB에 저장된 해시화된 비밀번호와는 일치하지 않았던 것입니다.

해결 과정:
초기 로그인 처리 방식은 MyBatis Mapper에서 사용자가 입력한 비밀번호(#{userPw})를 SQL 쿼리에 직접 포함하여, DB에 저장된 해시화된 비밀번호와 직접 비교하는 방식이었습니다.

기존 로그인 쿼리 : SELECT * FROM USER_TABLE WHERE user_id = #{userId} AND user_pw = #{userPw}
BCryptPasswordEncoder로 해시화된 비밀번호는 원본 평문 비밀번호와는 완전히 다른 형태를 가지며, 단순 SQL WHERE절 비교 쿼리문으로는 로그인이 되지 않았습니다. 
Spring Security는 내부적으로 등록된 PasswordEncoder(저의 경우 BCryptPasswordEncoder)를 조합하여 사용자 인증을 처리합니다.
문제의 원인은 바로 DB 쿼리 자체가 평문 비밀번호와 해시된 비밀번호를 직접 비교하는 잘못된 방식이었기 때문에 Spring Security 내부 인증 과정에서 비밀번호 불일치로 판단된 것이었습니다.

배운 점: 회원가입 시 서비스 계층에서 BCryptPasswordEncoder를 활용하여 회원가입 시에 사용자 비밀번호를 안전하게 해시화하고 데이터베이스에 저장하는 것의 중요성을 다시 한번 체감했습니다. 그리고 로그인 과정에서 Spring Security가 등록된 BCryptPasswordEncoder빈과 인증 메커니즘을 통해 사용자가 입력한 평문 비밀번호와 DB의 해시된 비밀번호를 자동으로 비교하고 검증한다는 것을 알게 되었습니다.
더 나아가 이런 개인정보 관련에서 비밀번호 해시화의 중요성도 배울 수 있었습니다.

