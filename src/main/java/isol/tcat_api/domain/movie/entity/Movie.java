package isol.tcat_api.domain.movie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@ToString
@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_movie_idx_generator", sequenceName = "seq_movie_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movie_idx_generator")
    @Column(name = "movie_idx", nullable = false)
    private long movieIdx;

    @Column(name = "movie_name", nullable = false, length = 100)
    private String movieName;

    @Column(name = "movie_img", length = 100)
    private String movieImg;

    @Column(name = "movie_age", nullable = false)
    private int movieAge;

    @Column(name = "movie_open_dt", nullable = false)
    private LocalDate movieOpenDt;

    @Column(name = "movie_description")
    private String movieDescription;

    @Column(name = "movie_time")
    private LocalTime movieTime;

    @Column(name = "is_del", length = 1)
    private String isDel;

}
