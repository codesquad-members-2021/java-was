
# 자바 웹 서버 만들기

## 진행 방법
- 요구사항 별로 Navigator와 Driver를 교체한다.
  - e.g.) 시온: 요구사항 1,3,5,7 / 제인: 2,4,6 등
- 인텔리제이 code with me 플러그인을 사용해서 코드를 함께 작성한다.
- 막히는 부분이 있을 시 각자 검색해 본 후 해결 방법을 공유한다.
- 인프런 HTTP 자료 등 학습 자료를 공유하고, 헷갈리는 개념은 서로 설명해준다. (페어와 동일한 유료 강의 각각 구매)

### Repository
- 공유하는 저장소 : https://github.com/janeljs/java-was

### Branch
||Local|Origin|Upstream|
|---|---|---|---|
|URL|  |janeljs/java-was|codesquad-members-2021|
|Branch|개인(예시: step1-j) |dev, ShionJane, 개인(예시: step1-s)|ShionJane|
|Rule|개인의 branch에서 작업|완성된 기능 dev에 push <br/> 코드 리뷰 후 머지받으면 ShionJane에 rebase|origin dev를 upstream ShionJane에 PR|

### 커밋 메시지 작성 규칙
- feat, refactor 등 커밋 템플릿 이용

<br/>

## 미션
## 1단계
### 요구사항 1
- http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### 요구사항 2
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다. 회원가입한다.
- 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
```
/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
```
- HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.

### 요구사항 3
- http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.

### 요구사항 4
- “회원가입”을 완료하면 /index.html 페이지로 이동하고 싶다. 현재는 URL이 /user/create 로 유지되는 상태로 읽어서 전달할 파일이 없다. 따라서 redirect 방식처럼 회원가입을 완료한 후 “index.html”로 이동해야 한다. 즉, 브라우저의 URL이 /index.html로 변경해야 한다.

### 요구사항 5
- “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다. 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
- 앞에서 회원가입한 사용자로 로그인할 수 있어야 한다. 로그인이 성공하면 cookie를 활용해 로그인 상태를 유지할 수 있어야 한다. 로그인이 성공할 경우 요청 header의 Cookie header 값이 logined=true, 로그인이 실패하면 Cookie header 값이 logined=false로 전달되어야 한다.

### 요구사항 6
- 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 로 접근했을 때 사용자 목록을 출력한다. 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

### 요구사항 7
- 지금까지 구현한 소스 코드는 stylesheet 파일을 지원하지 못하고 있다. Stylesheet 파일을 지원하도록 구현하도록 한다.

## 2단계 - 리팩토링

- 리팩토링 접근 방법 - 메소드 분리 및 클래스 분리
- 클라이언트 요청 데이터를 처리하는 로직을 별도의 클래스로 분리한다.(HttpRequest)
- 클라이언트 응답 데이터를 처리하는 로직을 별도의 클래스로 분리한다.(HttpResponse)
- 다형성을 활용해 클라이언트 요청 URL에 대한 분기 처리를 제거한다.
