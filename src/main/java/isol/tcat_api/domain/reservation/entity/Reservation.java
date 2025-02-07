package isol.tcat_api.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Builder
@Getter
@ToString
//@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_reservation_idx_generator", sequenceName = "seq_reservation_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reservation_idx_generator")
    @Column(name = "reservation_idx", nullable = false)
    private long reservationIdx;

    @Column(name = "user_idx", nullable = false)
    private long userIdx;

    @Column(name = "movie_room_mapping_idx", nullable = false)
    private long movieRoomMappingIdx;

    @Column(name = "reservation_cnt", nullable = false)
    private int reservationCnt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "reservation_seat", columnDefinition = "jsonb", nullable = false)
    private String reservationSeat;

}
