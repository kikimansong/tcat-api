package isol.tcat_api.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 공통 */
    ENTITY_NOT_FOUND(HttpStatus.OK, "COMMON-001", HttpStatus.NOT_FOUND.value(), "존재하지 않는 데이터입니다."),

    /* 인증 */
    AUTHENTICATION_FAILED(HttpStatus.OK, "AUTHENTICATION-001", HttpStatus.UNAUTHORIZED.value(), "인증이 실패했습니다."),

    /* 회원가입 */
    EMAIL_DUPLICATED(HttpStatus.OK, "SIGNUP-001", HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),

    /* 로그인 */
    LOGIN_FAILED(HttpStatus.OK, "SIGNIN-001", HttpStatus.UNAUTHORIZED.value(), "이메일 또는 비밀번호가 일치하지 않습니다."),

    /* 예매 */
    RESERVATION_FAILED_BY_NOT_OPEN(HttpStatus.OK, "RESERVATION-001", HttpStatus.INTERNAL_SERVER_ERROR.value(), "상영예정인 영화는 예매할 수 없습니다."),
    RESERVATION_FAILED_BY_DATETIME(HttpStatus.OK, "RESERVATION-002", HttpStatus.INTERNAL_SERVER_ERROR.value(), "예매는 상영 시간으로부터 2시간 전까지 가능합니다."),
    RESERVATION_FAILED_ALREADY_RESERVED(HttpStatus.OK, "RESERVATION-003", HttpStatus.INTERNAL_SERVER_ERROR.value(), "선택한 좌석이 이미 예매되었습니다."),

    REVIEW_FAILED_NOT_OPEN(HttpStatus.OK, "REVIEW-001", HttpStatus.INTERNAL_SERVER_ERROR.value(), "상영예정 작품에는 감상평을 등록할 수 없습니다."),
    REVIEW_FAILED_NO_RATING(HttpStatus.OK, "REVIEW-002", HttpStatus.INTERNAL_SERVER_ERROR.value(), "평점이 선택되지 않았습니다."),
    REVIEW_FAILED_NO_TEXT(HttpStatus.OK, "REVIEW-003", HttpStatus.INTERNAL_SERVER_ERROR.value(), "감상평이 입력되지 않았습니다.");

    private final HttpStatus httpStatus;
    private final String name;
    private final int code;
    private final String message;

}
