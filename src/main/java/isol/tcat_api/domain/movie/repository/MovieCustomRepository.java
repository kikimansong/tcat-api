package isol.tcat_api.domain.movie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.movie.dto.MovieDTO;
import isol.tcat_api.domain.movie.entity.QMovie;
import isol.tcat_api.domain.movieroommapping.entity.QMovieRoomMapping;
import isol.tcat_api.domain.reservation.entity.QReservation;
import isol.tcat_api.domain.review.entity.QReview;
import isol.tcat_api.global.entity.CommonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public MovieDTO.List<MovieDTO.Item> findList(String release, String movieName, CommonRequest commonRequest) {
        QMovie m = new QMovie("m");
        QReview r = new QReview("r");

        BooleanExpression movieNameWhere = null;
        BooleanExpression movieOpenDtWhere = null;

        /* 영화제목 검색 */
        if (movieName != null && !movieName.equals("")) {
            movieNameWhere = m.movieName.contains(movieName);
        }

        /* 상영여부 검색 */
        if (release != null && !release.equals("")) {
            switch (release) {
                case "0" :  // 상영중
                    movieOpenDtWhere = m.movieOpenDt.before(LocalDate.now());
                    break;

                case "1" :  // 상영예정
                    movieOpenDtWhere = m.movieOpenDt.after(LocalDate.now());
                    break;
            }
        }

        /* 페이징 설정 */
        final int pageLength = 12;  // 한 페이지당 아이템 개수
        final int pageStart = (commonRequest.getPage() - 1) * pageLength;

        List<MovieDTO.Item> queryList = jpaQueryFactory.select(Projections.fields(MovieDTO.Item.class,
                m.movieIdx,
                m.movieName,
                m.movieAge,
                m.movieImg,
                m.movieDescription,
                m.movieOpenDt,
                m.movieOpenDt.before(LocalDate.now()).as("isOpen"),
                m.movieTime,
                Expressions.numberTemplate(Double.class, "ROUND({0}, 1)", jpaQueryFactory
                        .select(r.rating.avg())
                        .from(r)
                        .where(r.movieIdx.eq(m.movieIdx))).as("totalRating")))
                .from(m)
                .where(
                        m.isDel.eq("N"),
                        movieNameWhere,
                        movieOpenDtWhere
                )
                .orderBy(
                        m.movieOpenDt.desc(),
                        m.movieIdx.desc()
                )
                .offset(pageStart)
                .limit(pageLength)
                .fetch();

        Long queryTotalCnt = jpaQueryFactory.select(m.movieIdx.count())
                .from(m)
                .where(
                        m.isDel.eq("N"),
                        movieNameWhere,
                        movieOpenDtWhere
                )
                .fetchOne();

        return MovieDTO.List.<MovieDTO.Item>builder()
                .list(queryList)
                .totalCnt(queryTotalCnt)
                .page(commonRequest.getPage())
                .lastPage((int) Math.ceil((double) queryTotalCnt.intValue() / pageLength))
                .pageStart(pageStart)
                .pageLength(pageLength)
                .build();
    }

    /**
     * 메인화면 영화 목록 조회 (예매율 Top 4)
     */
    public List<MovieDTO.Item> findMainList() {
        QMovie m = new QMovie("m");
        QMovieRoomMapping mrm = new QMovieRoomMapping("mrm");
        QReservation r = new QReservation("r");
        QReview rv = new QReview("rv");

        List<MovieDTO.Item> queryList = jpaQueryFactory
                .select(Projections.fields(MovieDTO.Item.class,
                        m.movieIdx,
                        m.movieName,
                        m.movieImg,
                        m.movieAge,
                        m.movieDescription,
                        m.movieOpenDt,
                        m.movieTime,
                        r.reservationCnt.castToNum(Long.class).sum().coalesce(0L).as("totalReservationCount"),
                        Expressions.numberTemplate(Double.class, "ROUND({0}, 1)", jpaQueryFactory
                                .select(rv.rating.avg())
                                .from(rv)
                                .where(rv.movieIdx.eq(m.movieIdx))).as("totalRating")))
                .from(m)
                .innerJoin(mrm).on(m.movieIdx.eq(mrm.movieIdx))
                .leftJoin(r).on(r.movieRoomMappingIdx.eq(mrm.movieRoomMappingIdx))
                .where(m.movieOpenDt.before(LocalDate.now()))
                .groupBy(m.movieIdx)
                .orderBy(
                        r.reservationCnt.sum().coalesce(0).desc(),
                        m.movieOpenDt.desc(),
                        m.movieIdx.desc()
                )
                .limit(4)
                .fetch();

        /* 초기 예매 데이터가 없을 시 아래 쿼리로 리스트 조회 */
        if (queryList.size() == 0) {
            queryList =
                    jpaQueryFactory
                            .select(Projections.fields(MovieDTO.Item.class,
                                    m.movieIdx,
                                    m.movieName,
                                    m.movieImg,
                                    m.movieAge,
                                    m.movieDescription,
                                    m.movieOpenDt,
                                    m.movieTime,
                                    r.reservationCnt.castToNum(Long.class).sum().coalesce(0L).as("totalReservationCount"),
                                    Expressions.numberTemplate(Double.class, "ROUND({0}, 1)", jpaQueryFactory
                                            .select(rv.rating.avg())
                                            .from(rv)
                                            .where(rv.movieIdx.eq(m.movieIdx))).as("totalRating")))
                            .from(m)
                            .leftJoin(mrm).on(m.movieIdx.eq(mrm.movieIdx))
                            .leftJoin(r).on(r.movieRoomMappingIdx.eq(mrm.movieRoomMappingIdx))
                            .where(m.movieOpenDt.before(LocalDate.now()))
                            .groupBy(m.movieIdx)
                            .orderBy(
                                    r.reservationCnt.sum().coalesce(0).desc(),
                                    m.movieOpenDt.desc(),
                                    m.movieIdx.desc()
                            )
                            .limit(4)
                            .fetch();
        }

        return queryList;
    }

    /**
     * 누적 관객 수 조회
     */
    public Long findTotalReservationCount(long movieIdx) {
        QReservation r = new QReservation("r");
        QMovieRoomMapping mrm = new QMovieRoomMapping("mrm");

        return jpaQueryFactory
                .select(r.reservationCnt.castToNum(Long.class).sum().coalesce(0L))    // 결과가 null 이면 0
                .from(mrm)
                .innerJoin(r).on(mrm.movieRoomMappingIdx.eq(r.movieRoomMappingIdx))
                .where(mrm.movieIdx.eq(movieIdx))
                .fetchOne();
    }

    /**
     * 평점 조회
     */
    public Double findTotalRating(long movieIdx) {
        QReview r = new QReview("r");

        return jpaQueryFactory
                .select(
                    Expressions.numberTemplate(Double.class, "ROUND({0}, 1)", r.rating.avg())
                )
                .from(r)
                .where(r.movieIdx.eq(movieIdx))
                .fetchOne();
    }

    /**
     * 예매 - 영화 목록 조회
     */
    public MovieDTO.List<MovieDTO.Item> findListOnReservation() {
        QMovie m = new QMovie("m");

        List<MovieDTO.Item> queryList = jpaQueryFactory.select(Projections.fields(MovieDTO.Item.class,
                        m.movieIdx,
                        m.movieName,
                        m.movieAge,
                        m.movieImg,
                        m.movieDescription,
                        m.movieOpenDt,
                        m.movieTime))
                .from(m)
                .where(
                        m.isDel.eq("N"),
                        m.movieOpenDt.before(LocalDate.now())
                )
                .orderBy(
                        m.movieOpenDt.desc(),
                        m.movieIdx.desc()
                )
                .fetch();

        Long queryTotalCnt = jpaQueryFactory.select(m.movieIdx.count())
                .from(m)
                .where(
                        m.isDel.eq("N"),
                        m.movieOpenDt.before(LocalDate.now())
                )
                .fetchOne();

        return MovieDTO.List.<MovieDTO.Item>builder()
                .list(queryList)
                .totalCnt(queryTotalCnt)
                .build();
    }

}
