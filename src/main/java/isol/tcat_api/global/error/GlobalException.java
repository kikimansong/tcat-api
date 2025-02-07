package isol.tcat_api.global.error;

import isol.tcat_api.global.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    ErrorCode errorCode;
}
