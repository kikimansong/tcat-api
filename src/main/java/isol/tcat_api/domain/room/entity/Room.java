package isol.tcat_api.domain.room.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@ToString
@NoArgsConstructor
@DynamicInsert
@SequenceGenerator(name = "seq_room_idx_generator", sequenceName = "seq_room_idx", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_room_idx_generator")
    @Column(name = "room_idx", nullable = false)
    private long roomIdx;

    @Column(name = "cinema_idx", nullable = false)
    private long cinemaIdx;

    @Column(name = "room_name", nullable = false, length = 10)
    private String roomName;

    @Column(name = "room_seat_cnt", nullable = false)
    private int roomSeatCnt;

    @Column(name = "room_blank_seat", columnDefinition = "jsonb" ,nullable = false)
    private String roomBlankSeat;

    @Column(name = "is_del", length = 1)
    private String isDel;

}
