package isol.tcat_api.domain.region.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.cinema.dto.CinemaDTO;
import isol.tcat_api.domain.cinema.entity.QCinema;
import isol.tcat_api.domain.region.dto.RegionDTO;
import isol.tcat_api.domain.region.entity.QRegion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegionCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<RegionDTO.ItemWithCount> findListOnReservation() {
        QRegion r = new QRegion("r");
        QCinema c = new QCinema("c");

        return jpaQueryFactory.select(Projections.fields(RegionDTO.ItemWithCount.class,
                        c.regionIdx,
                        r.regionName,
                        c.regionIdx.count().as("cinemaCount")
                ))
                .from(c)
                .leftJoin(r).on(c.regionIdx.eq(r.regionIdx))
                .where(c.isDel.eq("N"))
                .groupBy(c.regionIdx, r.regionName)
                .fetch();
    }

}
