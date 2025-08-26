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


### ⚠️ 트러블 슈팅 및 문제 해결
[사례 1] 스프링 시큐리티 BCryptPasswordEncoder 적용 후 로그인 실패 문제 해결
문제 상황: 회원가입 기능을 구현하며 Spring Security에서 제공하는 BCryptPasswordEncoder 빈(Bean)을 등록하고, 서비스 로직에서 이를 호출하여 비밀번호를 해시화하여 데이터베이스에 INSERT하도록 설정했습니다. DB에 저장된 비밀번호 값이 정상적으로 해시화되어 있음을 확인했지만, 회원가입 시 사용한 아이디와 비밀번호로 로그인 시도 시 지속적으로 로그인에 실패하는 문제가 발생했습니다.

원인 분석: 초기 로그인 로직은 MyBatis Mapper에서 사용자가 입력한 평문(Plaintext) 비밀번호(#{userPw})를 SQL 쿼리에 직접 포함하여, DB에 저장된 해시화된 값과 비교하는 방식이었습니다.

-- 기존 로그인 쿼리
SELECT * FROM USER_TABLE WHERE user_id = #{userId} AND user_pw = #{userPw};
그러나 BCryptPasswordEncoder로 해시화된 비밀번호는 원본 평문 비밀번호와는 완전히 다른 형태이며, 단순 SQL WHERE 절 비교로는 절대 일치하지 않습니다. 해시 함수는 단방향성을 가지므로, 사용자가 입력한 평문 비밀번호가 #{userPw}로 넘어와도 DB에 저장된 해시화된 비밀번호와는 비교 자체가 불가능했습니다.

해결 과정: 문제의 원인은 바로 DB 쿼리 자체가 평문 비밀번호와 해시된 비밀번호를 직접 비교하는 잘못된 방식이었기 때문에, Spring Security 내부 인증 과정에서 비밀번호 불일치로 판단된 것이었습니다.

원인 파악: 구글링을 통해 비밀번호 해시화의 본질과 Spring Security의 인증 플로우를 깊이 있게 파악했습니다. 특히, 해시화된 비밀번호는 평문과 직접적인 SQL 비교가 불가능하며, Spring Security가 등록된 PasswordEncoder를 활용하여 내부적으로 검증 로직을 수행한다는 것을 인지했습니다.

로그인 쿼리 수정: MyBatis Mapper에서는 더 이상 비밀번호를 쿼리 조건으로 사용하지 않고, 아이디(userId)만을 기준으로 사용자 정보 전체를 조회하도록 쿼리를 수정했습니다. 이로써 DB의 역할은 해당 아이디에 대한 사용자 데이터를 가져오는 것으로 한정되었습니다.

-- 수정된 로그인 쿼리
SELECT * FROM USER_TABLE WHERE user_id = #{userId};
이 수정된 쿼리를 통해 Spring Security의 UserDetailsService는 DB로부터 사용자 아이디에 해당하는 UserDetails(해시된 비밀번호 포함)를 성공적으로 로드할 수 있게 됩니다.

Spring Security의 자동 인증 메커니즘 활용: 쿼리 수정 후, 제가 별도로 BCryptPasswordEncoder의 matches() 메서드를 호출하는 코드를 추가하지 않았음에도 불구하고, Spring Security 프레임워크 내부의 DaoAuthenticationProvider가 등록된 BCryptPasswordEncoder를 사용하여 사용자가 입력한 평문 비밀번호와 DB에서 가져온 해시된 비밀번호를 올바르게 비교(matches() 메서드를 내부적으로 활용)하고 성공적으로 인증 처리하는 것을 확인했습니다.

배운 점: 이번 트러블 슈팅을 통해 회원가입 시 서비스 계층에서 BCryptPasswordEncoder를 활용하여 사용자 비밀번호를 안전하게 해시화하고 데이터베이스에 저장하는 것의 중요성을 다시 한번 체감했습니다. 그리고 로그인 과정에서 제가 직접 BCryptPasswordEncoder.matches() 메서드를 호출하는 코드를 작성하지 않아도, Spring Security가 등록된 BCryptPasswordEncoder 빈과 인증 메커니즘을 통해 사용자가 입력한 평문 비밀번호와 DB의 해시된 비밀번호를 자동으로 비교하고 검증한다는 사실을 명확히 이해하게 되었습니다. 더 나아가, 이 경험은 개인정보 보안에 있어 비밀번호 해시화의 절대적인 중요성과 함께, 각 컴포넌트(DAO, Service, Spring Security의 인증 로직) 간의 역할 분담과 상호 연동 방식, 그리고 프레임워크가 제공하는 자동화된 보안 기능의 가치를 깊이 있게 깨닫는 계기가 되었습니다.
