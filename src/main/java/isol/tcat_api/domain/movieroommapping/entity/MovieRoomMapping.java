package isol.tcat_api.domain.movieroommapping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_movie_room_mapping_idx_generator", sequenceName = "seq_movie_room_mapping_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "movie_room_mapping")
public class MovieRoomMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_movie_room_mapping_idx_generator")
    @Column(name = "movie_room_mapping_idx", nullable = false)
    private long movieRoomMappingIdx;

    @Column(name = "movie_idx", nullable = false)
    private long movieIdx;

    @Column(name = "room_idx", nullable = false)
    private long roomIdx;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

}
