<div align="center">

# Gold-Room-Auth

### ✨ Backend TechStack ✨
![Java](https://img.shields.io/badge/-Java-FF7800?style=flat&logo=Java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-flat&logo=spring&logoColor=white)
![SpringBoot](https://img.shields.io/badge/-SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white)
![SpringDataJPA](https://img.shields.io/badge/SpringDataJpa-236DB33F?style=flat&logo=spring&logoColor=white)
![SpringSecurity](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=Spring%20Security&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=flat&logo=MariaDB&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white)


### 🍀 서비스 개요 🍀
본 서비스는 gRPC 활용하여, 사용자 권한 인증을 진행합니다. </br>
회원가입, 로그인, 사용자 정보 조회, 금은방 정보 추가 서비스를 제공합니다.
</div>

</br>


## [목차]
- [Quick Start](#Quick-Start) 
- [📁 디렉토리 구조](#디렉토리-구조)
- [📦 ERD](#erd)
- [💌 API 명세서](#api-명세서)
- [✉ Git Commit Message Convention](#-git-commit-message-convention)
- [🌿 Git Branch 전략](#-git-branch-전략)

## Quick Start

## 디렉토리 구조
<details>
<summary><strong>구조도</strong></summary>
<div markdown="1">
  
```
src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── example
    │   │           └── goldroomauth
    │   │               ├── GoldRoomAuthApplication.java
    │   │               ├── config
    │   │               │   ├── JwtTokenProvider.java
    │   │               │   ├── SecurityConfig.java
    │   │               │   ├── SwaggerConfig.java
    │   │               │   └── TokenAuthenticationFilter.java
    │   │               ├── exception
    │   │               │   ├── BadRequestException.java
    │   │               │   ├── BaseException.java
    │   │               │   ├── ConflictException.java
    │   │               │   ├── ErrorCode.java
    │   │               │   ├── ErrorResponse.java
    │   │               │   ├── ForbiddenException.java
    │   │               │   ├── InternalServerException.java
    │   │               │   ├── NotFoundException.java
    │   │               │   ├── UnauthorizedException.java
    │   │               │   └── handler
    │   │               │       └── GlobalExceptionHandler.java
    │   │               └── user
    │   │                   ├── controller
    │   │                   │   ├── TokenController.java
    │   │                   │   ├── UserController.java
    │   │                   │   └── UserInfoController.java
    │   │                   ├── domain
    │   │                   │   ├── GoldRoom.java
    │   │                   │   ├── Token.java
    │   │                   │   ├── User.java
    │   │                   │   └── UserDetail.java
    │   │                   ├── dto
    │   │                   │   ├── AccessTokenResponse.java
    │   │                   │   ├── GoldRoomRequest.java
    │   │                   │   ├── GoldRoomResponse.java
    │   │                   │   ├── RefreshTokenRequest.java
    │   │                   │   ├── RefreshTokenResponse.java
    │   │                   │   ├── SignInRequest.java
    │   │                   │   ├── SignInResponse.java
    │   │                   │   ├── SignUpRequest.java
    │   │                   │   ├── SignUpResponse.java
    │   │                   │   └── UserInfoResponse.java
    │   │                   ├── repository
    │   │                   │   ├── GoldRoomRepository.java
    │   │                   │   ├── TokenRepository.java
    │   │                   │   └── UserRepository.java
    │   │                   └── service
    │   │                       ├── TokenService.java
    │   │                       ├── UserDetailService.java
    │   │                       ├── UserInfoService.java
    │   │                       ├── UserService.java
    │   │                       └── UserValidator.java
    │   └── resources
    │       ├── application-secret.yml
    │       └── application.yml
    └── test
        └── java
            └── org
                └── example
                    └── goldroomauth
                        └── GoldRoomAuthApplicationTests.java

```

</details>

## ERD
<img width="947" alt="인증구조" src="https://github.com/user-attachments/assets/2f39fdfe-7be4-45fe-83cc-23b7d2ee1ddc" width="90%" height="100%">

</br>

## API 명세서
</br>
<details>
<summary><strong>사용자 회원 가입</strong></summary>
<div markdown="1">

### 🙋‍♀️ 사용자 회원 가입
#### 1. 설명
- `계정명`, `패스워드`를 입력하여 회원가입 합니다.
</br>

#### 2. 입력
- url
> `POST` http://localhost:8888/api/users/sign-up

- body
```
{
  "username": "사용자ID",
  "password": "비밀번호",
  "nickname": "사용자 닉네임",
  "phoneNumber": "휴대전화번호",
  "zipCode": "우편번호",
  "address": "주소",
  "addressDetail": "상세주소"
}
```

#### 3. 출력
- **Response : 성공 시**
```
{
  "message": "회원가입이 완료되었습니다.",
  "username": "사용자ID",
  "nickname": "닉네임"
}
```

</br>
</details>

<details>
<summary><strong>사용자 로그인</strong></summary>
<div markdown="1">

### 🔍 사용자 로그인
#### 1. 설명
- `계정명`, `패스워드`로 로그인 시 JWT가 발급됩니다.
- 이후 모든 API 요청 Header에 JWT가 항시 포함되며, JWT 유효성을 검증합니다.

#### 2. 입력
- url
> `POST` http://localhost:8888/api/users/sign-in

- body
```
{
  "username": "계정",
  "password": "비밀번호"
}
```

#### 3. 출력
- **Response : 성공 시**
```
{
  "userId": "회원 고유아이디",
  "token": "액세스 토큰"
}
```

- **Response : 실패시**
- 올바르지 않은 비밀번호일 때
```
{
   "status": 400,
  "message": "올바르지 않은 비밀번호입니다."
}
```

- 존재하지 않는 아이디일 때
```
{
   "status": 404,
  "message": "존재하지 않는 사용자입니다."
}
```


</br>

</details>

<details>
<summary><strong> 사용자 금은방 설정 업데이트 </strong></summary>
<div markdown="1">

### ⭐ 사용자 금은방 설정 업데이트
#### 1. 설명
- 사용자가 판매자일 때 금은방 정보를 업데이트 합니다.

#### 2. 입력
- url
> `POST` http://localhost:8888/api/user-info/mypage/gold-room/add

- header
```
Authorization: “Bearer XXXXXXXXX”
```
- body
```
{
  "goldRoomName": "금은방 이름",
   "description": "설명",
   "goldRoomZipCode": "금은방 우편주소",
   "goldRoomAddress": "금은방 주소",
   "goldRoomAddressDetail": "금은방 상세주소"
}
```

#### 3. 출력
- **Response : 성공 시**
```
{
  "message": "금은방 정보가 성공적으로 등록됐습니다.",
  "goldRoomName": "금은방 이름",
  "description": "설명",
  "goldRoomZipCode": "금은방 우편주소",
  "goldRoomAddress": "금은방 주소",
  "goldRoomAddressDetail": "금은방 상세주소"
}
```

</br>

</details>

<details>
<summary><strong> 사용자 정보 조회 </strong></summary>
<div markdown="1">

### 📃 사용자 정보(API)
#### 1. 설명
- `패스워드`를 제외한 모든 사용자 정보를 반환합니다.
- 판매자일 때, 사용자의 금은방 정보를 사용하기 위해서입니다. 

#### 2. 입력
- url
> `GET` http://localhost:8888/api/user-info/mypage


#### 3. 출력
- **Response : 금은방 정보가 없는 경우**
```
{
  "message": "회원정보가 성공적으로 조회됐습니다. ",
  "username": "사용자Id",
  "nickname": "닉네임",
  "phoneNumber": "휴대전화",
  "zipCode": "우편번호",
  "address": "주소",
  "addressDetail": "상세주소",
  "goldRoomResponses": [
    {
      "message": "조회된 금은방이 없습니다.",
      "goldRoomName": "null",
      "description": "null",
      "goldRoomZipCode": "null",
      "goldRoomAddress": "null",
      "goldRoomAddressDetail": "null"
    }
  ]
}
```

- **Response : 금은방 정보가 있는 경우**
```
{
  "message": "회원정보가 성공적으로 조회됐습니다. ",
  "username": "사용자Id",
  "nickname": "닉네임",
  "phoneNumber": "휴대전화",
  "zipCode": "우편번호",
  "address": "주소",
  "addressDetail": "상세주소",
  "goldRoomResponses": [
    {
      "message": "등록하신 금은방 정보입니다.",
      "goldRoomName": "금은방 이름",
      "description": "설명",
      "goldRoomZipCode": "금은방 우편주소",
      "goldRoomAddress": "금은방 주소",
      "goldRoomAddressDetail": "금은방 상세주소"
    }
  ]
}
```

</div>
</details>

## ✉ Git Commit Message Convention
<details>
<summary>커밋 유형</summary>
<div markdown="1">
</br>
  <table>
    <tr>
      <th scope="col">커밋 유형</td>
      <th scope="col">의미</td>
    </tr>
    <tr>
      <td>feat</td>
      <td>새로운 기능 추가</td>
    </tr>
    <tr>
      <td>fix</td>
      <td>버그 수정</td>
    </tr>
    <tr>
      <td>docs</td>
      <td>문서 수정</td>
    </tr>
    <tr>
      <td>style</td>
      <td>코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우</td>
    </tr>
    <tr>
      <td>refactor</td>
      <td>코드 리팩토링</td>
    </tr>
    <tr>
      <td>test</td>
      <td>테스트 코드, 리팩토링 테스트 코드 추가</td>
    </tr>
    <tr>
      <td>chore</td>
      <td>패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore</td>
    </tr>
  </table>
  </br>
</div>
</details>

<details>
<summary>커밋 메세지 세부 내용</summary>
<div markdown="1">
</br>
&emsp;• 글로 작성하여 내용이 잘 전달될 수 있도록 할 것 </br></br>
&emsp;• 본문에는 변경한 내용과 이유 설명 (어떻게보다는 무엇 & 왜를 설명)</br>
&emsp;<div align="center" style="width:90%; height: 140px; border: 1px solid gray; border-radius: 1em; background-color: #AEADAB; color: black; text-align: left ">
&emsp;ex ) </br>
&emsp;refactor : 로그인 기능 변경 (title)</br>
&emsp;( 공 백 ) </br>
&emsp;기존 로그인 방식에서 ~~한 문제로 ~~한 방식으로 변경하였습니다. (content)
</br>
</br>
&emsp;feat : 로그인 기능 구현
</div>
</div>
</details>

## 🌿 Git Branch 전략
<details>
<summary>브렌치 명명 규칙</summary>
<div markdown="1">
</br>

`feat/기능명`

- ex)  feat/users_apply

</div>
</details>

<details>
<summary>브랜치 작성 방법</summary>
<div markdown="1">
</br>

- 브랜치는 기능 단위로 나눈다.
- feat 브랜치는 dev 브랜치에서 파생해서 만든다.
- PR을 통해 dev 브랜치에서 기능이 완성되면 main 브랜치로 merge 한다.

</br>

|이름|텍스트|
|----|-----|
|main|제품으로 출시될 수 있는 브랜치|
|dev|다음 출시 버전을 개발하는 브랜치|
|feat|기능을 개발하는 브랜치|

</div>
</details>

</br>
