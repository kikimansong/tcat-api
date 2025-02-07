package isol.tcat_api.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import isol.tcat_api.domain.user.entity.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    @Value("${jwt.expiration.access}") private int accessExp;
    @Value("${jwt.expiration.refresh}") private int refreshExp;

    /* application.yml의 jwt.secret 값을 불러와 key에 저장 */
    private JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /* User 정보를 가지고 AccessToken, RefreshToken을 생성하는 메소드 */
//    public JwtToken generateToken(Authentication authentication) {
//        // 권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        isol.tcat_api.domain.user.entity.User user
//                = (isol.tcat_api.domain.user.entity.User) authentication.getPrincipal();
//
//        long now = (new Date()).getTime();
//
//        // Date accessTokenExpiresIn = new Date(now + 86400000);
//        Date accessTokenExpiresIn = new Date(now + 10000);
//
//        String accessToken = Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim("auth", authorities)
//                .claim("userIdx", user.getUserIdx())
//                .claim("userEmail", user.getUserEmail())
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now + 86400000))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return JwtToken.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }

    public JwtToken generateToken(Authentication authentication) {
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(generateAccessToken(authentication))
                .refreshToken(generateRefreshToken())
                .build();
    }

    /* 액세스 토큰 생성 */
    public String generateAccessToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        isol.tcat_api.domain.user.entity.User user = customUserDetails.getUser();

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessExp);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("userIdx", user.getUserIdx())
                .claim("userEmail", user.getUserEmail())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* 리프레시 토큰 생성 */
    public String generateRefreshToken() {
        long now = (new Date()).getTime();

        return Jwts.builder()
                .setExpiration(new Date(now + refreshExp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* jwt 복호화 후 토큰에 들어있는 정보(claim)를 꺼내는 메소드 */
    public Authentication getAuthentication(String accessToken) {
        // jwt 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // claim에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        isol.tcat_api.domain.user.entity.User user = isol.tcat_api.domain.user.entity.User.builder()
                .userEmail(claims.getSubject())
                .userIdx(Long.parseLong(claims.get("userIdx").toString()))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return new UsernamePasswordAuthenticationToken(customUserDetails, "", authorities);
    }

    /* 토큰 정보를 검증하는 메소드 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 JWT 입니다.\n{}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 입니다.\n{}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 입니다.\n{}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claim이 비어있습니다.\n{}", e.getMessage());
        }

        return false;
    }

    /* accessToken */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /* 엑세스 토큰 재발급을 위한 오버로딩 */
    public String generateAccessToken(String authorities, long userIdx, String userEmail) {
        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + accessExp);

        return Jwts.builder()
                .setSubject(userEmail)
                .claim("auth", authorities)
                .claim("userIdx", userIdx)
                .claim("userEmail", userEmail)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
