package isol.tcat_api.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString(exclude = "userPw")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_user_idx_generator", sequenceName = "seq_user_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @Comment("user PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_idx_generator")
    @Column(name = "user_idx", nullable = false)
    private long userIdx;

    @Column(name = "user_email", nullable = false, length = 64, unique = true)
    private String userEmail;

    @Column(name = "user_pw", nullable = false, length = 64)
    private String userPw;

    /* getUsername()과 겹쳐서 네이밍 수정 */
    @Column(name = "user_name", nullable = false, length = 20)
    @JsonProperty("user_name")
    private String name;

    @Comment("user 전화번호 (010-0000-0000)")
    @Column(name = "user_phone", nullable = false, length = 13)
    private String userPhone;

    @Comment("user 생년월일 8자리 (2024-12-31)")
    @Column(name = "user_birth_dt", nullable = false)
    private LocalDate userBirthDt;

    @Column(name = "is_del", length = 1)
    private String isDel;

    @Column(name = "insert_at")
    private LocalDateTime insertAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    /* insert에 필요한 생성자 */
    @Builder
    public User(long userIdx, String userEmail, String userPw, String name, String userPhone, LocalDate userBirthDt) {
        this.userIdx = userIdx;
        this.userEmail = userEmail;
        this.userPw = userPw;
        this.name = name;
        this.userPhone = userPhone;
        this.userBirthDt = userBirthDt;
    }

}
