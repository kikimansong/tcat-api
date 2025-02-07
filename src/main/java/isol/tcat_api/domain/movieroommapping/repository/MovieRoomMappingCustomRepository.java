package isol.tcat_api.domain.movieroommapping.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieRoomMappingCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

}
