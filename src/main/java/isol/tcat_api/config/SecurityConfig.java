package isol.tcat_api.config;

import isol.tcat_api.global.jwt.JwtAccessDeniedHandler;
import isol.tcat_api.global.jwt.JwtAuthenticationEntryPoint;
import isol.tcat_api.global.jwt.JwtAuthenticationFilter;
import isol.tcat_api.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/v1/image/**").permitAll()
                                .requestMatchers("/api/v1/auth/token/refresh").permitAll()
                                .requestMatchers("/api/v1/user/signUp").permitAll()
                                .requestMatchers("/api/v1/user/signIn").permitAll()
                                .requestMatchers("/api/v1/movie/**").permitAll()
                                .requestMatchers("/api/v1/notice/**").permitAll()
                                .requestMatchers("/api/v1/region/**").permitAll()
                                .requestMatchers("/api/v1/movie-cinema-mapping/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/review/{movieIdx}").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* CORS 설정 */
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
//            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000"));
            config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://tcat-sigma.vercel.app"));
            config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "https://tcat-sigma.vercel.app"));
            config.setAllowCredentials(true);

            return config;
        };
    }

}
