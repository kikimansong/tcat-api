package isol.tcat_api.domain.reservation.respository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.reservation.dto.ReservationDTO;
import isol.tcat_api.domain.reservation.entity.QReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ReservationDTO.ReservedSeatsItem> findReservedSeats(long movieRoomMappingIdx) {
        QReservation r = new QReservation("r");

        return jpaQueryFactory
                .select(Projections.fields(ReservationDTO.ReservedSeatsItem.class,
                        r.reservationCnt,
                        r.reservationSeat))
                .from(r)
                .where(
                        r.movieRoomMappingIdx.eq(movieRoomMappingIdx)
                )
                .fetch();
    }

}
