# accountApp
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

# 설계
## 기본적인 구조

## 인증 로직

 인증 과정은 jwt와 인터셉터를 활용하여 이뤄 집니다. 로그인 시 access token과 refresh token이 발급 되며 기본적으로 어세스 토큰으로 인증과 인가가 이뤄지는데, 어쎄스토큰 만료시
refresh 토큰을 사용자 정보를 식별하여 access token을 재발급합니다. 가계부와 관련된 url로 접근시 인증이 이뤄지며, 인증 실패 시 로그인 페이지로 넘어가게 됩니다.
 토큰을 활용한 인증을 인터셉터와 db만을 활용하였지만 추후 스프링 시큐리티와 redis를 구현하는 것이 좋을 것 같다고 생각합니다.
 
 ## api
 RESTFUL api를 구현하기 위해 http 메서드 4가지를 모두 구현하였고, ResponseEntity를 사용하여 응답 staus를 활용하고자 했지만 부족하다고 생각합니다.
 ### accounting 도메인
 * "/accounting/report"
  * GET : 가계부 내역을 취득합니다.
  * POST : 가계부 내역을 등록합니다.
  * DELETE : 가계부 내역을 삭제합니다.
  * PUT : 등록된 가계부를 수정합니다.
* "/account/costPieChart"
  * GET : 소득 비중 차트를 그리기위한 데이터를 가져옵니다.
* "/user"
  * GET : 유저의 아이디와 기본 정보를 취득합니다.
  * POST : 회원 가입시 유저를 등록합니다. 패스워드는 암호화 되어 저장됩니다.
* "/verifyUser"
  * GET :아이디의 중복여부를 확인합니다.
* "/signout"
  * GET : 로그아웃 합니다.
 
 
 
