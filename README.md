# accountApp
# app 설명
 지출과 수입을 기록할 수 있는 가계부 어플리케이션 입니다. 
제한된 시간 내에서 요구 사항을 기술로 풀어 내기 위해 
안 해본 것, 할 수 있는 것, 필수적으로 알아야 하는 것을
나눠 시간을 효율적으로 활용하기 위해 노력했습니다.
 최대한 간결하게 코드를 작성하고, 사용자가 편리하기 이용할 수 있도록
간결하게 구현했습니다.
 

## 실행방법
### 밑 준비
우분투 환경
<pre>
sudo apt update
sudo apt install git
sudo apt install docker.io -y
sudo apt install docker-compose -y
</pre>

### 설치
<pre>
cd /home
git clone https://github.com/kiki3700/accountApp.git
cd /accountApp
</pre>

### 실행
<pre>
sudo docker-compose up -d
</pre>

## 개발 환경
* os : unbuntu 20.3
* BE : java 11, spring boot 2.6, myBatis
* FE : html5, css3, js, jquery
* DB : mysql 5.7
* etc : docker, docker-compose, maven, jwt

## 필수 요구사항
* 고객은 이메일과 비밀번호 입력을 통해서 회원 가입을 할 수 있습니다.
* 고객은 회원 가입이후, 로그인과 로그아웃을 할 수 있습니다.
  * 가계부에 오늘 사용한 돈의 금액과 관련 메모를 남길 수 있습니다.
  * 가계부에서 수정을 원하는 내역은 금액과 메모를 수정할 수 있습니다.
  * 가계부에서 삭제를 원하는 내역은 삭제 할 수 있습니다.
  * 삭제한 내역은 언제든지 다시 복구 할 수 있어야 한다.
  * 가계부에서 이제까지 기록한 가계부 리스트를 볼 수 있습니다.
  * 가계부에서 상세한 세부 내역을 볼 수 있습니다.
* 로그인하지 않은 고객은 가계부 내역에 접근 제한 처리가 되어야 합니다.

## 추가로 구현한 사항
* 월별 가계부 내역 확인 기능
* 페이징 처리
* 구글 차트를 사용해 지출 부증 분석

## 미구현된 기능
* 현재 까지의 지출과 수입, 잔액 
* 월별 수입 지출 분석 그래프
* css


# 설계
## 기본적인 구조
![KakaoTalk_20220117_175923608](https://user-images.githubusercontent.com/74307591/149739100-7ac27e21-3239-46a2-abd0-b051c5bd2c52.png)

## 인증 로직
![인증](https://user-images.githubusercontent.com/74307591/149739255-7ab8833c-7b8a-46b9-bcb8-4462e5cc3bed.png)
 인증 과정은 jwt와 인터셉터를 활용하여 이뤄 집니다. 로그인 시 access token과 refresh token이 발급 되며 기본적으로 어세스 토큰으로 인증과 인가가 이뤄지는데, 어쎄스토큰 만료시
refresh 토큰을 사용자 정보를 식별하여 access token을 재발급합니다. 가계부와 관련된 url로 접근시 인증이 이뤄지며, 인증 실패 시 로그인 페이지로 넘어가게 됩니다.
 토큰을 활용한 인증을 인터셉터와 db만을 활용하였지만 추후 스프링 시큐리티와 redis를 구현하는 것이 좋을 것 같다고 생각합니다.
 
 ## api
 RESTFUL api를 구현하기 위해 http 메서드 4가지를 모두 구현하였고, ResponseEntity를 사용하여 응답 staus를 활용하고자 했지만 부족하다고 생각합니다.
 ### accounting 도메인
 * "/accounting
  * GET : 가계부 내역을 취득합니다.
   * uri : /reports/{userId} 
  * POST : 가계부 내역을 등록합니다
   * uri : /report/{userId}
  * DELETE : 가계부 내역을 삭제합니다.
   * uri : /report 
  * PUT : 등록된 가계부를 수정합니다.
   * uri : /report 
* "/account/costPieChart"
  * GET : 소득 비중 차트를 그리기위한 데이터를 가져옵니다.
### user 도메인
* "/user"
  * GET : 유저의 아이디와 기본 정보를 취득합니다.
  * POST : 회원 가입시 유저를 등록합니다. 패스워드는 암호화 되어 저장됩니다.
* "/verifyUser"
  * GET :아이디의 중복여부를 확인합니다.
* "/signout"
  * GET : 로그아웃 합니다.

# 리펙터링 계획
 - interceptor를 이용해서 auth 관련 처리를 한 부분은 좋습니다.
    - ⇒ redis + spring security + jpa 를 써서 리패토링 할 것이다.  +TDD
- 컨트롤러에 로그를 남기는 것은 좋지만 로그와 실행코드가 섞여 있어서 가독성이 좋지 않은 것 같습니다. 로그 남기는 기능을 하는 코드를 따로 작성하는 것이 좋을 것 같습니다.
    - ⇒ aop를 통한 logger 구현
- 클라이언트의 요청에 따라 서버에서 어떻게 처리가 되었는지를 알려주는 HTTP status code에 대해 학습하시면 좋을 것 같습니다.
- RESTful API 관련 URL 표현에 대해서 학습하시면 좋을것 같습니다.
 
 
