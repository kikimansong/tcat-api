package isol.tcat_api.domain.auth.controller;

import isol.tcat_api.domain.auth.service.RefreshTokenService;
import isol.tcat_api.global.entity.CommonResponse;
import isol.tcat_api.global.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<?>> logout(@RequestHeader("Authorization") final String accessToken) {
        // 엑세스 토큰으로 현재 redis 정보 삭제
        refreshTokenService.removeRefreshToken(accessToken);
        return ResponseEntity.ok(CommonResponse.builder().code(200).message("success").build());
    }

    /**
     * 토큰 재발급
     * @param accessToken 액세스 토큰
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") final String accessToken) {
        String newAccessToken = refreshTokenService.accessTokenRefresh(accessToken);

        if (newAccessToken == null) {
            return ResponseEntity.ok(ErrorResponse.builder()
                    .status(HttpStatus.OK.value())
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("리프레시 토큰이 만료되었습니다.")
                    .build());
        }

        return ResponseEntity.ok(CommonResponse.builder().code(200).message("success").data(newAccessToken).build());
    }

}
