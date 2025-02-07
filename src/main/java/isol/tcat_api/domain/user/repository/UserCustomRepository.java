package isol.tcat_api.domain.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.user.dto.UserDTO;
import isol.tcat_api.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserDTO.Info findUserByUserIdx(long userIdx) {
        QUser u = new QUser("u");

        return jpaQueryFactory.select(Projections.fields(UserDTO.Info.class,
                u.userIdx,
                u.userEmail,
                u.name))
                .from(u)
                .where(
                        u.isDel.eq("N"),
                        u.userIdx.eq(userIdx)
                )
                .fetchOne();
    }

}
