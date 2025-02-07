package isol.tcat_api.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * return 값이 필요한 예외
 */
@Getter
@AllArgsConstructor
public class ExtendedGlobalException extends RuntimeException {
    ErrorCode errorCode;
    Object data;
}
