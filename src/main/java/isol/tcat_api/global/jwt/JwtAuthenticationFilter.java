package isol.tcat_api.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    /* 필터를 거치지 않는 url 목록 */
    private final List<String> excludeUrl = List.of(
            "/api/v1/image/**",
            "/api/v1/user/signUp",
            "/api/v1/user/signIn",
            "/api/v1/movie/**",
            "/api/v1/notice/**",
            "/api/v1/region/**",
            "/api/v1/movie-cinema-mapping/**",
            "/api/v1/review/{movieIdx}"
    );

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        // Request Header에서 JWT 추출
//        String token = resolveToken((HttpServletRequest) request);
//
//        // validateToken으로 토큰 유효성 검사
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }

    /**
     * GenericFilterBean 대신 OncePerRequestFilter 상속을 받아 doFilterInternal 메소드 오버라이드
     * Servlet은 다른 Servlet으로 disptch 되는 경우가 있음
     * GenericFilterBean을 상속 후 JWT 인증 수행 시 앞에 수행되었던 filter를 다시 거치는 경우가 생겨 자원낭비 발생
     * OncePerRequestFilter는 어느 서블릿 컨테이너든 요청당 한 번의 실행을 보장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 요청 url이 excludeUrl에 포함되어 있으면 필터 로직을 건너 뜀
        if (excludeUrl.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()))) {
            chain.doFilter(request, response);
            return;
        }

        // Request Header에서 JWT 추출
        String token = resolveToken(request);

        // validateToken으로 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getRequestURI().contains("/api/v1/auth/token/refresh");
//    }

    /* Request Header에서 토큰 정보 추출 */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
