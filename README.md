# T오더 백엔드 개발과제

https://clover-slice-59f.notion.site/T-d3ff92e1f75e46cd945d12a60f50e077

## 설계구조

DB

| 메뉴목록(food_menu) |
| --- |
| 주문목록(order_menu) |
| 결제목록(pay) |
| 사용자목록(user) |

![폴더 구조](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ac2032da-95c5-404a-b792-e4438acd7e53/스크린샷_2022-04-01_오전_7.43.07.png)

폴더 구조

## 코드설명

@builder : 객체의 생성 방법과 표현 방법을 분리하기 위해 사용

@Configuration : 설정 파일을 만들기 위해 사용

@NoArgsConstructor : 생성자를 자동으로 생성해줌, 파라미터가 없는 생성자를 생성

@AllArgsConstructor : 클래스에 존재하는 모든 필드에 대한 생성자를 자동으로 생성해줌

@Autowired : 필요한 의존 객체의 “타입"에 해당하는 빈을 찾아 주입

```
//domain
@JsonIgnoreProperties({"orderMenus"})
@JoinColumn(name = "username")
@ManyToOne
privateUseruser;
```

- .js
    
    ```
    beforeSend: function (xhr) {
    xhr.setRequestHeader('Authorization', 'Bearer ' + localStorage.getItem('token'));
    },
    ```
    
    ```
    //결제 목록
    functionlastlist() {
        $.ajax({
            url:"/main/lastlist",
            type:"GET",
            success: function (getpaylist) {
                var totalPrice = 0;
                $('#paylist').empty();
                for (var i = 0; i <getpaylist.length; i++){
                totalPrice +=getpaylist[i].count *getpaylist[i].price;
                    $('#paylist').append(
                        '<div> <span>' +getpaylist[i].menu_name + '</span>'
                        + '<span>' +getpaylist[i].count + '</span>'
                        + '<span class = "payPrice">' +getpaylist[i].price + '</span>'
                        + '</div>'
                        );
                    }
                    $('#paylist').append(
                    '<span id = "totalTitle">총금액</span>'
                    + '<span id = "totalPrice">' + totalPrice + '</span>'
                    );
            }, error: function (e) {
                console.log(e);
            }
        })
    }
    ```
    

**JWT(잘 모르겠어요,,)**

build.gradle →implementation 'io.jsonwebtoken:jjwt:0.9.1' ,
implementation 'org.springframework.boot:spring-boot-starter-security'  추가

application.properties →*jwt.secret*=javainuse 추가

- JwtTokenUtil
    
    ```java
    package com.homework.torder.util;
    
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Component;
    
    import java.io.Serializable;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.function.Function;
    
    @Component
    public class JwtTokenUtil implements Serializable {
    
        private static final long serialVersionUID = -2550185165626007488L;
        public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    
        @Value("${jwt.secret}")
        private String secret;
    
        //jwt 토큰에서 사용자 이름 검색
        public String getUsernameFromToken(String token) {
            return getClaimFromToken(token, Claims::getSubject);
        }
    
        //jwt 토큰에서 만료 날짜 검색
        public Date getExpirationDateFromToken(String token) {
            return getClaimFromToken(token, Claims::getExpiration);
        }
    
        public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        }
        //토큰에서 정보를 검색하려면 비밀번호필요
        private Claims getAllClaimsFromToken(String token) {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }
    
        //토큰이 만료되었는지 확인
        private Boolean isTokenExpired(String token) {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        }
    
        //사용자에 대한 토큰 생성
        public String generateToken(UserDetails userDetails) {
            Map<String, Object> claims = new HashMap<>();
            return doGenerateToken(claims, userDetails.getUsername());
        }
    
        //while creating the token -
        //1. 토큰의 클레임을 정의
        //2. HS512 알고리즘과 비밀 키를 사용하여 JWT에 로그인(?)
        //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
        //   JWT를 URL 안전 문자열로 압축
        private String doGenerateToken(Map<String, Object> claims, String subject) {
    
            return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                    .signWith(SignatureAlgorithm.HS512, secret).compact();
        }
    
        //토큰 확인
        public Boolean validateToken(String token, UserDetails userDetails) {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
    }
    ```
    
- UserApiController
    
    ```java
    @RequiredArgsConstructor
    @RestController
    public class UserApiController {
    
        private final JwtTokenUtil jwtTokenUtil;
        private final AuthenticationManager authenticationManager;
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;
    
        @RequestMapping(value = "/login", method = RequestMethod.POST)
        public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto userDto) throws Exception {
            System.out.println(userDto.getUsername() + userDto.getPassword());
            System.out.println(passwordEncoder.encode(userDto.getPassword()));
    
            authenticate(userDto.getUsername(), userDto.getPassword());
    
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
        }
    
        private void authenticate(String username, String password) throws Exception {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            } catch (DisabledException e) {
                throw new Exception("USER_DISABLED", e);
            } catch (BadCredentialsException e) {
                throw new Exception("INVALID_CREDENTIALS", e);
            }
        }
    }
    ```
    
- JwtAuthenticationEntryPoint
    
    ```java
    @Component
    public classJwtAuthenticationEntryPointimplementsAuthenticationEntryPoint{
    
    @Override
    public void commence(HttpServletRequestrequest,HttpServletResponseresponse,
                             AuthenticationExceptionauthException) throwsIOException{
    
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
    ```
    
- JwtAuthenticationFilter
    
    ```java
    @RequiredArgsConstructor
    @Component
    public classJwtAuthenticationFilterextends OncePerRequestFilter {
    
        private finalUserDetailsServiceuserDetailsService;
        private finalJwtTokenUtiljwtTokenUtil;
    
    StringHEADER_STRING = "Authorization";
    StringTOKEN_PREFIX = "Bearer ";
    
    @Override
    protected void doFilterInternal(HttpServletRequestreq,HttpServletResponseres,FilterChainchain) throwsIOException,ServletException{
    Stringheader =req.getHeader(HEADER_STRING);
    Stringusername = null;
    StringauthToken = null;
            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                authToken = header.replace(TOKEN_PREFIX,"");
                try {
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                } catch (IllegalArgumentExceptione) {
                    logger.error("an error occured during getting username from token",e);
                } catch (ExpiredJwtExceptione) {
                    logger.warn("the token is expired and not valid anymore",e);
                } catch(SignatureExceptione){
                    logger.error("Authentication Failed. Username or Password not valid.");
                }
            } else {
                logger.warn("couldn't find bearer string, will ignore the header");
            }
            if (username != null &&SecurityContextHolder.getContext().getAuthentication() == null) {
    
    UserDetailsuserDetails = userDetailsService.loadUserByUsername(username);
    
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
    UsernamePasswordAuthenticationTokenauthentication = new UsernamePasswordAuthenticationToken(userDetails, null,Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    logger.info("authenticated user " + username + ", setting security context");
    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
    
    chain.doFilter(req,res);
        }
    }
    ```
    
- UserDetailsServiceImpl
    
    ```java
    @RequiredArgsConstructor
    @Service
    public classUserDetailsServiceImplimplementsUserDetailsService{
    
        private finalUserRepositoryuserRepository;
    
        publicUserDetailsloadUserByUsername(Stringusername) throwsUsernameNotFoundException{
    Useruser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));
    
            return new UserDetailsImpl(user);
        }
    }
    ```
    
- WebSecurityConfig
    
    ```java
    @RequiredArgsConstructor
    @Configuration
    @EnableWebSecurity// 스프링 Security 지원을 가능하게 함
    @EnableGlobalMethodSecurity(securedEnabled = true)
    public classWebSecurityConfigextends WebSecurityConfigurerAdapter {
    
        private finalJwtAuthenticationEntryPointjwtAuthenticationEntryPoint;
        private finalJwtAuthenticationFilterjwtRequestFilter;
    
    @Override
    protected void configure(HttpSecurityhttp) throwsException{
    http.csrf().disable();
    http.headers().frameOptions().disable();
    
    http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    
    http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
        }
    
    @Bean
    publicBCryptPasswordEncoderencodePassword() {
            return new BCryptPasswordEncoder();
        }
    
    @Bean
        @Override
    publicAuthenticationManagerauthenticationManagerBean() throwsException{
            return super.authenticationManagerBean();
        }
    
    }
    ```
    

## **요구사항** ( 결과화면 )

- **사용자 로그인/로그아웃**
    
    **SpringSecurity + JWT를 사용해 로그인 구현**
    
    ![로그인 화면](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c39031ac-b942-48b8-b7c5-f35d91ede3b2/스크린샷_2022-04-01_오전_6.47.41.png)
    
    로그인 화면
    
    ![로그인 완료](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e8051739-71ab-41fe-afe5-6e631fa78720/스크린샷_2022-04-01_오전_6.49.21.png)
    
    로그인 완료
    
    ![로그아웃 버튼을 누르면 로그아웃 되면서 로그인 화면으로 돌아가게 됨.](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a6f35165-e22f-49f6-bc04-eb15d07b2ae7/스크린샷_2022-04-01_오전_6.53.55.png)
    
    로그아웃 버튼을 누르면 로그아웃 되면서 로그인 화면으로 돌아가게 됨.
    
    ![로그아웃 완료 창](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d2e084f4-005e-4b99-82f8-afad4d116c8b/스크린샷_2022-04-01_오전_6.55.04.png)
    
    로그아웃 완료 창
    
    ![로그인 하지 않고 index접근 했을때](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/db58d744-4d80-4656-b903-e9cccd055e13/스크린샷_2022-04-01_오전_6.57.30.png)
    
    로그인 하지 않고 index접근 했을때
    
- **메뉴 10개 이상 조회 (메뉴 DB 필요)**
    
    ![메뉴 조회와 메뉴 DB](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ababd131-c9a7-49cf-b131-08cbc62b9b05/스크린샷_2022-04-01_오전_7.04.14.png)
    
    메뉴 조회와 메뉴 DB
    
- **메뉴 선택 (장바구니에 메뉴 삽입)**
    
    ![메뉴 클릭시 장바구니에 메뉴 삽입](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e5dd4efe-b431-49ee-8957-d8b92b311243/스크린샷_2022-04-01_오전_7.07.05.png)
    
    메뉴 클릭시 장바구니에 메뉴 삽입
    
- **메뉴 취소 (장바구니에 메뉴 삭제)**
    
    ![삭제버튼 클릭시 항목 삭제](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8a137e6b-3c4a-4771-b372-3e86bee0e93b/스크린샷_2022-04-01_오전_7.09.50.png)
    
    삭제버튼 클릭시 항목 삭제
    
- **장바구니 (temporary 저장소로 DB 저장 필요 X)**
    
    ![전역변수 menuCart사용하여 db사용X](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ec5413cc-743a-4dc0-8c7c-8601edec4154/스크린샷_2022-04-01_오전_7.12.22.png)
    
    전역변수 menuCart사용하여 db사용X
    
- **주문 (주문 후 장바구니 삭제 / 주문 내역 DB 필요)**
    
    ![주문하기 누르면 장바구니 삭제 및 order_menue테이블에 insert](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d1d7d361-8faa-41e5-bc62-2cd191fda58d/스크린샷_2022-04-01_오전_7.15.08.png)
    
    주문하기 누르면 장바구니 삭제 및 order_menue테이블에 insert
    
    ![주문 목록](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/88c90111-02c8-4d3a-8187-15fbcdb83a3a/스크린샷_2022-04-01_오전_7.16.55.png)
    
    주문 목록
    
- **결제 (결제 후 주문 내역 삭제 / 결제 내역 DB 필요)**
