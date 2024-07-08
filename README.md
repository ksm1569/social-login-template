# OAuth2 Social Login Template
![image](https://github.com/ksm1569/social-login-template/assets/34292113/51eaf7b6-db94-44cb-8f9a-d5195ffb7ca0)

![image](https://github.com/ksm1569/social-login-template/assets/34292113/4c88f218-0508-42ac-97c9-21bbb3858f03)


사이드 프로젝트 시 소셜 로그인으로 구성을 많이 한다.

템플릿으로 사용할 수 있도록 다양한 스프링부트 버전으로 구현하였다.

구글, 네이버, 카카오 로그인 3가지 유형으로 만들어보았다.

우리 서버에서 jwt access token을 발급하여 클라이언트 쿠키에 담고, refresh token은 db에 저장하여

access토큰은 만료되었으나 refresh토큰은 만료되지 않았을 경우 재발급을 해주게끔 처리하였다.

</br>
부하관련이슈가 있을 시

refresh token을 저장하고 있는 rdb를 redis가 그 역할을 하게끔 변경해주면 된다.

**소셜로그인 api key들은 깃허브 push를 위해 jasypt로 암호화하였다.**
</br>
</br>

## 개발환경 - 해당 브랜치
* Spring boot 2.7.12
* oauth2
* jwt 0.11.5
* thymeleaf
* Java 17
* JPA & mysql 8.0.34
* lombok
* gradle



## 기본 셋팅
mysql 8.0.34 설치 후 아래의 쿼리문 차례로 실행
```
CREATE DATABASE notifycal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'notifycal_user'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON notifycal.* TO 'notifycal_user'@'localhost';
FLUSH PRIVILEGES;
```

```
create table users (
    id bigint not null auto_increment,
    email varchar(255) not null,
    password varchar(255),
    name varchar(255),
    image_url varchar(255),
    provider varchar(255),
    provider_id varchar(255),
    unique_identifier varchar(255) not null,
    refresh_token TEXT,
    refreshTokenExpiryDate datetime(6),
    created_at datetime(6),
    updated_at datetime(6), primary key (id)
) engine=InnoDB;

alter table users add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table users add constraint UK_1mh0m6c9yk2vkvm9wsnhnegb7 unique (unique_identifier);
```

</br>
</br>

### 1. 구글 로그인 (https://console.cloud.google.com/)

* **위 링크에 접속해서 구글 로그인을 해준다.**


![image](https://github.com/ksm1569/social-login-template/assets/34292113/de57efa3-3555-436e-9868-08f53fac0675)

* **새 프로젝트를 클릭하여 적당한 이름으로 만들어준다.** </br></br>

![image](https://github.com/ksm1569/social-login-template/assets/34292113/0ae63e40-7d8e-4a8e-a3a4-6bfef3ee0b05)

* **API 및 서비스 - OAuth 동의화면을 선택한다.**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/1f3f1104-348d-442c-9938-f9d0180c155c)

</br></br>
* **External 선택 후 만들기**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/c2c2a658-ec74-47a0-9a49-090904b2219a)

</br></br>
* **필수 항목만 넣어주면 된다. 항목 넣어준 후 나머지 step은 저장후 계속**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/098fb03e-9f7a-4a25-a656-ae2b8856d1b7)

</br></br>
* **사용자 인증 정보로 넘어간다**

  **사용자 인증 정보 만들기 - OAuth 클라이언트 ID 선택**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/1a380bb0-d848-4a9a-a712-8d70b4911e21)

</br></br>
* **제일 중요한 부분이다**

  **웹 애플리케이션 선택 후 아래의 URI를 승인된 리디렉션 URI에 추가해주자**
```
  http://localhost:5050/login/oauth2/code/google
```

![image](https://github.com/ksm1569/social-login-template/assets/34292113/acedcc3b-9c3e-41fb-a440-651ff47daeac)

</br></br>
* **client-id와 client-secret를 발급해준다. 스프링부트 yml쪽에 사용 할 것이니 잘 저장해두자**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/c629a365-3e43-4b22-be5f-d689cff1041f)


</br>
</br>

### 2. 네이버 로그인 (https://developers.naver.com/apps)

* **위 링크에 접속해서 네이버 로그인을 해준다.**

* **애플리케이션 등록을 클릭하여 적당한 이름으로 만들어준다.** </br></br>
![image](https://github.com/ksm1569/social-login-template/assets/34292113/bcab58f5-d24b-4d51-8f9f-c1f4ee743b2d)

* **사용 API에 네이버 로그인을 클릭해준다**
![image](https://github.com/ksm1569/social-login-template/assets/34292113/0d9af9d7-0a70-4bf5-9718-8220752ac60d)

</br>

* **제공 정보를 선택해주고, 로그인 오픈 API 서비스환경을 PC웹으로 해준다**
![image](https://github.com/ksm1569/social-login-template/assets/34292113/9137a9c7-c911-4a8f-8682-9a84893d26dd)


</br>
</br>

* **가장 중요한 서비스URL과 Callback URL을 써주고 저장을 누르자**
```
http://localhost:5050
http://localhost:5050/login/oauth2/code/naver
```

![image](https://github.com/ksm1569/social-login-template/assets/34292113/61b2b836-ccb9-463b-973d-313e69cb65b5)


</br></br>
* **client-id와 client-secret를 발급해준다. 스프링부트 yml쪽에 사용 할 것이니 잘 저장해두자**
![image](https://github.com/ksm1569/social-login-template/assets/34292113/c6cef07e-da26-4f29-81b5-fe8088795d74)



</br>
</br>

### 3. 카카오 로그인 (https://developers.kakao.com/console/app)

* **위 링크에 접속해서 카카오 로그인을 해준다.**

* **애플리케이션 추가하기를 클릭하여 적당한 이름으로 만들어준다.** </br></br>
![image](https://github.com/ksm1569/social-login-template/assets/34292113/cd804b26-4483-4799-9a9f-3b44d8d7c05b)
![image](https://github.com/ksm1569/social-login-template/assets/34292113/c62d388a-22d6-4f7e-9c58-e7064560885a)

</br></br>
* **만든 애플리케이션을 클릭해준다.**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/060ecd02-9c68-4190-b585-f35dab8de1fc)

</br></br>
* **박스쳐둔 7곳을 참조하거나 설정을 하면된다**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/c4c605df-0577-48a8-852d-a1f62059eea6)

</br></br>
* **카카오로그인 활성화**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/ee5514ef-4526-4b45-a40d-2a511a00c130)

</br>

* **동의항목 설정**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/d9d4b90a-3759-4718-baf9-b599cc047041)


</br>

* **Redirect URI설정**
```
http://localhost:5050/login/oauth2/code/kakao
```
![image](https://github.com/ksm1569/social-login-template/assets/34292113/6c2b629f-255f-4904-adcf-bf43ce31a07b)

</br>

* **설정은 끝났고 client-id와 client-secret를 복사만 하면된다**

**카카오에서 client-id는 앱키 탭에 REST API키가 이다.**
![image](https://github.com/ksm1569/social-login-template/assets/34292113/38e30a1c-5c00-4442-827d-9e0f36e000dc)

</br>

**카카오에서 client-secret는 보안탭에 들어가서 생성해줘야한다.**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/297d3ed4-ab96-406c-b61d-710517e20df5)

</br>
</br>

이제 개발자센터에서 설정할거는 끝났다.

소스를 clone받아보면 JasyptConfigTest.java 라는 테스트코드가 있을거다.

**key부분에 원하는 key를 넣고, str부분에 아까 받았던 소셜로그인쪽의 비밀키를 넣어서 하나씩 암호화 시켜서 따로 저장해두자**

![image](https://github.com/ksm1569/social-login-template/assets/34292113/da6262cc-2c82-4b85-801d-94cfc216e55a)

<br>

**application.yml 파일을 열어 암호화된 문자열들을 하나씩 ENC()로 감싸서 넣어주면 된다.**
![image](https://github.com/ksm1569/social-login-template/assets/34292113/fe3f0d64-49ee-4c00-b11d-7ed3d4a90b13)

<br>

**그리고 아까 지정해뒀던 jasypt 키설정은 아래와 같다**

**edit configuration을 클릭 - Modify Options 클릭 - Add VM Options 클릭**

**까지하면 입력칸이 하나 추가된다 거기에 아래처럼 아까 지정한 키를 추가해주면 완료다**
```
-Djasypt.encryptor.password=key input
```

</br>

![image](https://github.com/ksm1569/social-login-template/assets/34292113/6c797115-0fba-4306-b642-d7b81e478a4b)

![image](https://github.com/ksm1569/social-login-template/assets/34292113/61a22498-313c-45d8-9c29-0166d8b25a13)

![image](https://github.com/ksm1569/social-login-template/assets/34292113/e22a88ee-c907-44e5-9a4c-f3fb947cbc0b)

