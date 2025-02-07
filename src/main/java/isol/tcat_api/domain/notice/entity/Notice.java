package isol.tcat_api.domain.notice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_notice_idx_generator", sequenceName = "seq_notice_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notice_idx_generator")
    @Column(name = "notice_idx", nullable = false)
    private long noticeIdx;

    @Column(name = "notice_tp", nullable = false)
    private int noticeTp;

    @Column(name = "notice_title", nullable = false, length = 200)
    private String noticeTitle;

    @Column(name = "notice_contents", nullable = false)
    private String noticeContents;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "banner_img", length = 100)
    private String bannerImg;

    @Column(name = "is_banner", length = 1)
    private String isBanner;

    @Column(name = "is_del", length = 1)
    private String isDel;

    @Column(name = "insert_at")
    private LocalDateTime insertAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
