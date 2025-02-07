package isol.tcat_api.domain.moviecinemamapping.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.movie.entity.QMovie;
import isol.tcat_api.domain.moviecinemamapping.dto.MovieCinemaMappingDTO;
import isol.tcat_api.domain.moviecinemamapping.entity.QMovieCinemaMapping;
import isol.tcat_api.domain.movieroommapping.entity.QMovieRoomMapping;
import isol.tcat_api.domain.reservation.entity.QReservation;
import isol.tcat_api.domain.room.entity.QRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieCinemaMappingCustomRepository {

//    private final JPAQueryFactory jpaQueryFactory;

//    public List<MovieCinemaMappingDTO.ReservationItem> findListOnReservation(long movieIdx, long cinemaIdx, LocalDate startAt) {
//        QMovieCinemaMapping mcm = new QMovieCinemaMapping("mcm");
//        QMovie m = new QMovie("m");
//        QRoom r = new QRoom("r");
//        QMovieRoomMapping mrm = new QMovieRoomMapping("mrm");
//        QReservation rs = new QReservation("rs");
//
//        /* 현재 시간 + 2시간 (상영 시작 시간이 현재 시간보다 2시간 미만으로 남으면 예매 불가) */
//        LocalDateTime nowTimePlus2Hours = LocalDateTime.now().plusHours(2);
//
//        return jpaQueryFactory.select(Projections.fields(MovieCinemaMappingDTO.ReservationItem.class,
//                        mrm.movieRoomMappingIdx,
//                        r.roomIdx,
//                        m.movieName,
//                        r.roomName,
//                        r.roomSeatCnt,
//                        mrm.startAt,
//                        ExpressionUtils.as(r.roomSeatCnt.castToNum(Long.class).subtract(JPAExpressions
//                                .select(rs.reservationCnt.sum().coalesce(0).castToNum(Long.class))
//                                .from(rs)
//                                .where(rs.movieRoomMappingIdx.eq(mrm.movieRoomMappingIdx))), "possibleSeatCnt"),
//                        ExpressionUtils.as(
//                                Expressions.booleanTemplate(
//                                        "CASE WHEN {0} < {1} THEN false ELSE true END",
//                                        mrm.startAt, Expressions.constant(nowTimePlus2Hours)
//                                ), "isAvailable")
//                ))
//                .from(mcm)
//                .innerJoin(m).on(mcm.movieIdx.eq(m.movieIdx))
//                .innerJoin(r).on(mcm.cinemaIdx.eq(r.cinemaIdx))
//                .innerJoin(mrm).on(mrm.movieIdx.eq(m.movieIdx).and(mrm.roomIdx.eq(r.roomIdx)))
//                .where(
//                        m.isDel.eq("N"),
//                        r.isDel.eq("N"),
//                        mcm.movieIdx.eq(movieIdx),
//                        mcm.cinemaIdx.eq(cinemaIdx),
//                        Expressions.dateTemplate(LocalDate.class, "CAST({0} AS date)", mrm.startAt).eq(startAt)
//                )
//                .orderBy(
//                        mrm.startAt.asc(),
//                        mrm.movieRoomMappingIdx.asc()
//                )
//                .fetch();
//    }


}
