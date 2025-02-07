package isol.tcat_api.domain.user.controller;

import isol.tcat_api.domain.user.dto.UserDTO;
import isol.tcat_api.domain.user.service.UserService;
import isol.tcat_api.global.entity.CommonResponse;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<CommonResponse<?>> getList() {
        List<UserDTO> list = userService.getList();
        return new ResponseEntity<>(CommonResponse.builder()
                .code(1)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CommonResponse<?>> getUser(@RequestHeader("Authorization") final String accessToken) {
        UserDTO.Info userInfo = userService.getUser(accessToken);

        // DB에서 회원 조회가 안되는 경우(회원탈퇴 등...) 예외 처리
        if (userInfo == null) {
            throw new GlobalException(ErrorCode.AUTHENTICATION_FAILED);
        }

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(userInfo)
                .build(), HttpStatus.OK);
    }

    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public ResponseEntity<CommonResponse<?>> create(@RequestBody UserDTO.Create param) {
        userService.create(param);
        return new ResponseEntity<>(CommonResponse.builder()
                .code(201)
                .message("회원가입이 완료되었습니다.")
                .build(), HttpStatus.CREATED);
    }

    /**
     * 로그인
     */
    @PostMapping("/signIn")
    public ResponseEntity<CommonResponse<?>> signIn(@RequestBody UserDTO.SignIn param) {
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("로그인 성공")
                .data(userService.signIn(param))
                .build(), HttpStatus.OK);
    }

}
