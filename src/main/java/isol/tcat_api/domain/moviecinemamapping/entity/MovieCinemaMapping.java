package isol.tcat_api.domain.moviecinemamapping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_movie_cinema_mapping_idx_generator", sequenceName = "seq_movie_cinema_mapping_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "movie_cinema_mapping")
public class MovieCinemaMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movie_cinema_mapping_idx_generator")
    @Column(name = "movie_cinema_mapping_idx", nullable = false)
    private long movieCinemaMappingIdx;

    @Column(name = "movie_idx", nullable = false)
    private long movieIdx;

    @Column(name = "cinema_idx", nullable = false)
    private long cinemaIdx;

}
