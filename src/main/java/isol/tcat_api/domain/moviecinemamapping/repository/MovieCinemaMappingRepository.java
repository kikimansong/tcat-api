package isol.tcat_api.domain.moviecinemamapping.repository;

import isol.tcat_api.domain.moviecinemamapping.entity.MovieCinemaMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieCinemaMappingRepository extends JpaRepository<MovieCinemaMapping, Long> {

    @Query(value = "" +
            "SELECT" +
            "   mrm.movie_room_mapping_idx, " +
            "   r.room_idx, " +
            "   r.room_name, " +
            "   r.room_seat_cnt, " +
            "   m.movie_name, " +
            "   mrm.start_at, " +
            "   CAST(mrm.start_at + (EXTRACT(EPOCH FROM m.movie_time) * INTERVAL '1 second') AS TIMESTAMP) AS end_at, " +
            "   (CAST(r.room_seat_cnt AS bigint)) - " +
            "   ( " +
            "       SELECT " +
            "           CAST(COALESCE(sum(rs.reservation_cnt), 0) AS bigint) " +
            "       FROM reservation rs " +
            "       WHERE rs.movie_room_mapping_idx = mrm.movie_room_mapping_idx " +
            "   ) AS possibleSeatCnt, " +
            "   CASE " +
            "       WHEN mrm.start_at < (now() + INTERVAL '2 hours') " +
            "       THEN FALSE " +
            "       ELSE TRUE " +
            "   END AS isAvailable " +
            "FROM movie_cinema_mapping mcm " +
            "   INNER JOIN movie m " +
            "   ON m.movie_idx = mcm.movie_idx " +
            "" +
            "   INNER JOIN room r " +
            "   ON r.cinema_idx = mcm.cinema_idx " +
            "" +
            "   INNER JOIN movie_room_mapping mrm " +
            "   ON mrm.movie_idx = m.movie_idx AND mrm.room_idx = r.room_idx " +
            "WHERE 1=1 " +
            "   AND m.is_del = 'N' " +
            "   AND r.is_del = 'N' " +
            "   AND mcm.movie_idx = :movieIdx " +
            "   AND mcm.cinema_idx = :cinemaIdx " +
            "   AND cast(mrm.start_at AS date) = :startAt " +
            "ORDER BY " +
            "   mrm.start_at ASC, " +
            "   mrm.movie_room_mapping_idx ASC", nativeQuery = true)
    List<Object[]> findListOnReservation(
            @Param("movieIdx") long movieIdx,
            @Param("cinemaIdx") long cinemaIdx,
            @Param("startAt") LocalDate startAt);

}
