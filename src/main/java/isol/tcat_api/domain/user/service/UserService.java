package isol.tcat_api.domain.user.service;

import isol.tcat_api.domain.auth.entity.RefreshToken;
import isol.tcat_api.domain.auth.repository.RefreshTokenRepository;
import isol.tcat_api.domain.user.dto.UserDTO;
import isol.tcat_api.domain.user.entity.CustomUserDetails;
import isol.tcat_api.domain.user.entity.User;
import isol.tcat_api.domain.user.repository.UserCustomRepository;
import isol.tcat_api.domain.user.repository.UserRepository;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.GlobalException;
import isol.tcat_api.global.jwt.JwtToken;
import isol.tcat_api.global.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public List<UserDTO> getList() {
        List<User> userList =  userRepository.findAll();

        System.out.println("############################");
        System.out.println(userList.size());
        System.out.println("############################");

        List<UserDTO> userDTOList = userList.stream()
                .map(user -> UserDTO.builder()
                        .userIdx(user.getUserIdx())
                        .userEmail(user.getUserEmail())
                        .userName(user.getName())
                        .userPhone(user.getUserPhone())
                        .userBirthDt(user.getUserBirthDt())
                        .build())
                .collect(Collectors.toList());

        return userDTOList;
    }

    @Transactional
    public UserDTO.Info getUser(String accessToken) {
        String nonBearerAccessToken = "";

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer")) {
            nonBearerAccessToken = accessToken.substring(7);
        }

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(nonBearerAccessToken);

        if (refreshToken.isPresent()) {
            return userCustomRepository.findUserByUserIdx(refreshToken.get().getUserIdx());
        }

        return null;
    }

    /**
     * 이메일 중복체크
     */
    public Boolean checkDuplicate(String userEmail) {
        boolean isDuplicate = false;

        if (0 < userRepository.countByUserEmail(userEmail))
            isDuplicate = true;

        return isDuplicate;
    }

    @Transactional
    public void create(UserDTO.Create param) {
        if (checkDuplicate(param.getUserEmail())) {
            throw new GlobalException(ErrorCode.EMAIL_DUPLICATED);
        }

        userRepository.save(User.builder()
                .userEmail(param.getUserEmail())
                .userPw(passwordEncoder.encode(param.getUserPw()))
                .name(param.getUserName())
                .userPhone(param.getUserPhone())
                .userBirthDt(param.getUserBirthDt())
                .build());
    }

    @Transactional
    public JwtToken signIn(UserDTO.SignIn param) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(param.getUserEmail(), param.getUserPw());

        try {
            // authenticate 메소드가 실행될 때 loadUserByUsername 메소드 실행
            Authentication authentication
                    = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            User user = customUserDetails.getUser();

            // 로그인한 유저의 토큰을 redis에 저장
            refreshTokenRepository.save(
                    new RefreshToken(
                            user.getUserIdx(),
                            user.getUserEmail(),
                            jwtToken.getAccessToken(),
                            jwtToken.getRefreshToken())
            );

            return jwtToken;
        } catch (BadCredentialsException e) {
            throw new GlobalException(ErrorCode.LOGIN_FAILED);
        }
    }

}
