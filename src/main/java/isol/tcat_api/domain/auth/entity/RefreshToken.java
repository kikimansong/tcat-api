package isol.tcat_api.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/* Redis 리프레시 토큰 만료기간 */
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 7)    // 7일
@AllArgsConstructor
@Getter
public class RefreshToken implements Serializable {

    @Id
    private Long userIdx;

    private String userEmail;

    @Indexed
    private String accessToken;

    private String refreshToken;

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
