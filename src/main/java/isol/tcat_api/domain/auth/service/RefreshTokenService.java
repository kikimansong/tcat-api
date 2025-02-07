package isol.tcat_api.domain.auth.service;

import isol.tcat_api.domain.auth.entity.RefreshToken;
import isol.tcat_api.domain.auth.repository.RefreshTokenRepository;
import isol.tcat_api.global.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void saveTokenInfo(long userIdx, String userEmail, String accessToken, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(userIdx, userEmail, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        String nonBearerAccessToken = "";

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
            nonBearerAccessToken = accessToken.substring(7);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(nonBearerAccessToken)
                .orElseThrow(IllegalAccessError::new);

        refreshTokenRepository.delete(refreshToken);
    }

    /**
     * 토큰 재발급
     */
    @Transactional
    public String accessTokenRefresh(String accessToken) {
        String nonBearerAccessToken = "";

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
            nonBearerAccessToken = accessToken.substring(7);
        }

        // 액세스 토큰으로 리프레시 토큰 조회
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(nonBearerAccessToken);

        // 리프레시 토큰이 존재하고 유효할 시
        if (refreshToken.isPresent() && jwtTokenProvider.validateToken(refreshToken.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내 옴
            RefreshToken resultToken = refreshToken.get();

            // 권한(Role) 추출
            String authorities = jwtTokenProvider.getAuthentication(nonBearerAccessToken)
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            // 새 액세스 토큰 생성
            String newAccessToken = jwtTokenProvider.generateAccessToken(
                    authorities,
                    resultToken.getUserIdx(),
                    resultToken.getUserEmail());

            resultToken.updateAccessToken(newAccessToken);

            refreshTokenRepository.save(resultToken);

            return newAccessToken;
        }

        return null;
    }

}
