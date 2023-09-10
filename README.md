# 톰캣 구현하기

## 요구사항

### 1단계

- [x] GET /index.html 요청에 대한 응답을 반환한다.
- [x] CSS를 지원한다.
- [x] Query String을 파싱한다.
    - [x] 아이디, 비밀번호가 일치하면 콘솔창에 로그로 회원을 조회한 결과를 출력한다.

### 2단계

- [x] 로그인 여부에 따라 다른 페이지로 이동시킨다.
    - [x] 로그인에 성공하면 응답 헤더에 http status code를 302로 반환하고 /index.html로 리다이렉트 한다.
    - [x] 로그인에 실패하면 401.html로 리다이렉트한다.
- [x] POST 방식으로 회원가입을 한다.
    - [x] 회원가입 페이지는 GET으로 요청한다.
    - [x] 회원가입을 완료하면 index.html로 리다이렉트한다.
    - [x] 로그인 페이지도 버튼을 눌렀을 때 POST 방식으로 전송하도록 변경한다.
- [x] 서버에서 HTTP 응답을 전달할 때 응답 헤더에 Set-Cookie를 추가한다.
    - [x] Cookie에 JSESSIONID가 없으면 응답 헤더에 Set-Cookie를 반환해준다.
- [x] 쿠키에서 전달 받은 JSESSIONID의 값으로 로그인 여부를 체크한다.
    - [x] 로그인된 상태에서 /login 페이지에 접근하면 index.html 페이지로 리다이렉트 처리한다.

### 3단계

- [x] HttpRequest 클래스 구현하기
- [x] HttpResponse 클래스 구현하기
- [x] Controller 인터페이스 추가하기

### 4단계

- [x] Executors로 Thread Pool 적용
- [x] 동시성 컬렉션 사용하기
