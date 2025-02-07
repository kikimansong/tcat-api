package isol.tcat_api.domain.user.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {

    /* 폐기 예정 */
    private long userIdx;
    private String userEmail;
    private String userName;
    private String userPhone;
    private LocalDate userBirthDt;

    /* User 생성 UserDTO.Create */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Create {
        private String userEmail;
        private String userPw;
        private String userName;
        private String userPhone;
        private LocalDate userBirthDt;
    }

    /* User 로그인 UserDTO.SignIn */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class SignIn {
        private String userEmail;
        private String userPw;
    }

    /* 로그인 중인 User 데이터 UsertDTO.Info */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Info {
        private long userIdx;
        private String userEmail;
        private String name;
    }

}
