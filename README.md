# 프로젝트 소개

모두의 가계부 백엔드

달력에 하루의 지출을 기록하고 관리할 수 있는 

시연 영상 : [https://www.youtube.com/watch?v=KRfb9lRpzdU](https://www.youtube.com/watch?v=KRfb9lRpzdU)

서비스 주소 : [http://lipton-web.shop.s3-website.ap-northeast-2.amazonaws.com](http://lipton-web.shop.s3-website.ap-northeast-2.amazonaws.com/)

## 조원

### 백엔드

- 남우식 ([https://github.com/namusik](https://github.com/namusik))
- 양희준 ([https://github.com/gogoheejun](https://github.com/gogoheejun))
- 하원빈 ([https://github.com/woodimora](https://github.com/woodimora))

### 프론트엔드

- 최진식 ([https://github.com/lipton-web](https://github.com/lipton-web))
- 백승엽 ()
- 양주영 ([https://github.com/yangddu](https://github.com/yangddu))

## 기술 스택

- Spring Boot
- AWS
- MySQL
- JPA
- Spring Security

## 모델링

- 데이터베이스 스키마
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d836b08f-5e41-4ac5-a5b7-17466769e2b0/Untitled.png)
    
- 엔티티 연관관계(JPA)
    
    User
    
    ```java
    @Entity
    public class User extends Timestamped{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @Column(nullable = false, unique = true)
        private String username;
    
        @Column(nullable = false)
        private String password;
    
        @Column
        private String sex;
    
        @Column
        private Long age;
    
        @Column
        private String job;
    
        @Column
        private Long salary;
    
        @Column(nullable = false)
        @Enumerated(value = EnumType.STRING)
        private UserRoleEnum role;
    
        @OneToMany(mappedBy = "user")
        @JsonIgnore
        private List<Record> records;
    
        @OneToMany(mappedBy = "toUser")
        @JsonIgnore
        private List<Follow> followers;
    
        @OneToMany(mappedBy = "fromUser")
        @JsonIgnore
        private List<Follow> followings;
    
        @Column(unique = true)
        private Long kakaoId;
    ```
    
    Record
    
    ```java
    @Entity
    public class Record extends Timestamped{
        @Id
        @GeneratedValue
        @Column(name = "RECORD_ID")
        private Long id;
    
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "USER_ID")
        private User user;
    
        @Column(nullable = false)
        private Long cost;
    
        @Column(nullable = false)
        private String contents;
    
        @Column(nullable = false)
        private String category;
    
        @Column(nullable = false)
        private LocalDate date;
    ```
    
    Follow
    
    ```java
    @Entity
    public class Follow extends Timestamped {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @JoinColumn(name="FROM_USER_ID")
        @ManyToOne(fetch = FetchType.LAZY)
        private User fromUser;
    
        @JoinColumn(name="TO_USER_ID")
        @ManyToOne(fetch = FetchType.LAZY)
        private User toUser;
    ```
    
- Api
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/f8873e39-ed09-4052-a6bb-e043697d4ac6/Untitled.png)
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/6b1cf942-fba0-4db8-8ba9-746b8fcda936/Untitled.png)
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0aec41c9-f51c-4640-979b-cce4acd721ea/Untitled.png)
    
    ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/25476ee5-6eeb-4eb1-adc8-721e02b78bf3/Untitled.png)
    

## 트러블 슈팅

- 구독한 피드만 Page<Record> 타입으로 리턴하기
    - 처음시도:
        
        ```java
        @Query(value = "SELECT * FROM record WHERE USER_ID IN (SELECT TO_USER_ID FROM Follow WEHRE FROM_USER_ID = :userId);", nativeQuery = true)
            List<Record> getFollowingFeeds(Long userId);
        ```
        
        문제점: 리턴타입이 List이다. Page로 받아야 하는 문제가 있다.
        
        - 현재방법: FeedService에서 userList를 일단 다 받아온 후, 매개변수로 넣어주는 방식. 이렇게 하면 Pageable을 매개변수에 넣을 수 있기에 간편하게 Page<>로 리턴받을 수 있다.
        
        ```java
        Page<Record> findAllByUserIn(List<User> list, Pageable pageable);
        ```
        
        단점: List로 다 받아온 것을 가지고 또한번 db에서 가져온거랑 비교해야 한다.효율적이지 않은 것 같다. 추후 개선해야 한다.
        
- CORS관련
    
    WebConfig.java에 다음과 같이 설정을 해주었으나, WebSecurityConfig.java에는 추가 설정해주지 않아 계속 문제가 발생했다.
    
    ```java
    @Configuration
    public class Webconfig implements WebMvcConfigurer {
    
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    //클라이언트 로컬 주소임. 클라이언트에서 내 서버의 api에 접근 시 허용에 관한 부분. CORS.
                    //2개 이상의 origin에 대해서 허용할 수 있음!
                    .allowedMethods("POST","GET","PUT","DELETE","HEAD","OPTIONS") // 클라이언트에서 요청하는 메소드 어디까지 허용할 것인가.
                    .allowCredentials(true);
        }
    
        @Bean
        public TomcatContextCustomizer sameSiteCookiesConfig() {
            return context -> {
                final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
                cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
                context.setCookieProcessor(cookieProcessor);
            };
        }
    }
    ```
    
    시큐리티파일에도 설정을 추가해줘야 한다.
    
    WebSecurityConfig.java
    
    ```java
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors();
    ```
    
- 로그인 성공/실패 커스터마이징해서 응답하기
    
    [https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=stpark89&logNo=221621540986](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=stpark89&logNo=221621540986)
    
    위 링크를 참고하여 해결. 그러나 jwt방식 로그인을 쓰면서 쓰지 않게 되었다.
    
- 스프링부트 JWT 로그인 방식 적용
    - 1.  JwtAuthenticationFilter를 통해 Request Header의 JWT 추출
        
        ```java
        @RequiredArgsConstructor
        public class JwtAuthenticationFilter extends GenericFilterBean {
        
            private final JwtTokenProvider jwtTokenProvider;
        
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                    // 헤더에서 JWT 를 받아옵니다.
                    String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
                System.out.println(token);
                // 유효한 토큰인지 확인합니다. 유효성검사
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    // 토큰 인증과정을 거친 결과를 authentication이라는 이름으로 저장해줌.
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
        
                    // SecurityContext 에 Authentication 객체를 저장합니다.
                    // token이 인증된 상태를 유지하도록 context(맥락)을 유지해줌
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                chain.doFilter(request, response);
            }
        }
        ```
        
    - 2. JwtTokenProvider → JWT 토큰 생성 및 인증 역할
        
        ```java
        @RequiredArgsConstructor
        @Component
        public class JwtTokenProvider {
        
            private String secretKey = "moneybook";
        
            // 토큰 유효시간 30분
            //////// FrontEnd와 약속해서 일치시켜야 하는 부분 /////////
            private long tokenValidTime = 240 * 60 * 1000L;
        
            private final UserDetailsService userDetailsService;
        
            // 객체 초기화, secretKey를 Base64로 인코딩한다.
            @PostConstruct
            protected void init() {
                secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
            }
        
            // JWT 토큰 생성
            public String createToken(String userPk) {
                Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        //        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
                Date now = new Date();
                return Jwts.builder()
                        .setClaims(claims) // 정보 저장
                        .setIssuedAt(now) // 토큰 발행 시간 정보
                        .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                        // FrontEnd와 일치하게 설정해주기. 아마 그래야 FrontEnd와 통신에 문제 없음 //
                        .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                        // signature 에 들어갈 secret값 세팅
                        .compact();
            }
        
            // JWT 토큰에서 인증 정보 조회
            public Authentication getAuthentication(String token) {
                System.out.println(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
                return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                // 추출하면 username, email등 유저의 정보가 나오게 됨.
            }
        
            // 토큰에서 회원 정보 추출
            public String getUserPk(String token) {
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            }
        
            // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
            public String resolveToken(HttpServletRequest request) {
        //        String token = null;
        //        Cookie cookie = WebUtils.getCookie(request, "X-AUTH-TOKEN");
        //        if(cookie != null) token = cookie.getValue();
                return request.getHeader("X-AUTH-TOKEN");
            }
        
            // 토큰의 유효성 + 만료일자 확인  // -> 토큰이 expire되지 않았는지 True/False로 반환해줌.
            public boolean validateToken(String jwtToken) {
                try {
                    Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
                    System.out.println(claims); // JWT 토큰(클라이언트에서 보낸)이 잘 들어오는지 검증하는 부분 -> 서버 콘솔에 token 찍힘.
                    return !claims.getBody().getExpiration().before(new Date()); // expire시간이 되지 않았다면 True!
                } catch (Exception e) {
                    return false;
                }
            }
        }
        ```
        
    - 3.위 필터를 WebSecurityConfig 파일에서 활용
        
        ```java
        public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        
            private final JwtTokenProvider jwtTokenProvider;
        
        		...
        		....
        		protected void configure(HttpSecurity http) throws Exception {
                http.cors();
                http.headers().
        					...
        				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                                UsernamePasswordAuthenticationFilter.class);
            }
        ```
        
    - 4.UserController에서 로그인성공시 토큰 부여,
        
        ```java
        // 로그인
            @PostMapping("/api/users/login")
            @ResponseBody
            public ResponseDto login(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
                System.out.println(username);
                System.out.println(password);
                User user = userService.login(username, password);
                String checkUsername = user.getUsername();
                UserRoleEnum roleEnum = user.getRole();
        
                String token = jwtTokenProvider.createToken(checkUsername);
                response.setHeader("X-AUTH-TOKEN", token);
        
                //header에 cookie 저장도 해주고
                Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.addCookie(cookie);
        
                //body에도 보내주기 혹시모르니까
                return new ResponseDto("success","로그인 성공했습니다",token);
            }
        ```
        
    - 5.이제 모든 요청의 헤더는 token을 함께 보내고, JwtAuthenticationFilter가 입구에서 검사해서 인증정보를 SecurityContextHolder에 저장해준다.
        
        ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/bae48797-29eb-4c93-8deb-3cb15aff3145/Untitled.png)
