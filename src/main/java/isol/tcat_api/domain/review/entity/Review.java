package isol.tcat_api.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@DynamicInsert
@SequenceGenerator(name = "seq_review_idx_generator", sequenceName = "seq_review_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_review_idx_generator")
    @Column(name = "review_idx", nullable = false)
    private long reviewIdx;

    @Column(name = "movie_idx", nullable = false)
    private long movieIdx;

    @Column(name = "user_idx", nullable = false)
    private long userIdx;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "insert_at")
    private LocalDateTime insertAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

}
