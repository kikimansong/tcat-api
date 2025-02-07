package isol.tcat_api.domain.cinema.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@SequenceGenerator(name = "seq_cinema_idx_generator", sequenceName = "seq_cinema_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "cinema")
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cinema_idx_generator")
    @Column(name = "cinema_idx", nullable = false)
    private long cinemaIdx;

    @Column(name = "region_idx", nullable = false)
    private long regionIdx;

    @Column(name = "is_del", length = 1)
    private String isDel;

    @Column(name = "cinema_name", length = 50)
    private String cinemaName;

}
