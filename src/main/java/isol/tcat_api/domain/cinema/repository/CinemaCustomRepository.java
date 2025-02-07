package isol.tcat_api.domain.cinema.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.cinema.dto.CinemaDTO;
import isol.tcat_api.domain.cinema.entity.QCinema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CinemaCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CinemaDTO.Item> findByRegionIdxIn(List<Long> regionIdxList) {
        QCinema c = new QCinema("c");

        return jpaQueryFactory.select(Projections.fields(CinemaDTO.Item.class,
                        c.regionIdx,
                        c.cinemaIdx,
                        c.cinemaName
                ))
                .from(c)
                .where(
                        c.isDel.eq("N"),
                        c.regionIdx.in(regionIdxList)
                )
                .orderBy(c.cinemaIdx.desc())
                .fetch();
    }

}
