package isol.tcat_api.domain.review.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.review.dto.ReviewDTO;
import isol.tcat_api.domain.review.entity.QReview;
import isol.tcat_api.domain.user.entity.QUser;
import isol.tcat_api.global.entity.CommonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * movieIdx의 기준으로 마지막 reivewIdx 가져오기
     */
    public Long findLastIdxByMovieIdx(long movieIdx) {
        QReview r = new QReview("r");

        Long queryLastIdx = jpaQueryFactory
                .select(r.reviewIdx)
                .from(r)
                .where(
                        r.movieIdx.eq(movieIdx)
                )
                .orderBy(
                        r.insertAt.desc(),
                        r.reviewIdx.desc()
                )
                .limit(1)
                .fetchOne();

        if (queryLastIdx == null) {
            queryLastIdx = 0L;
        }

        return queryLastIdx;
    }

    public List<ReviewDTO.Item> findList(long movieIdx, CommonRequest commonRequest, long lastReviewIdx) {
        QReview r = new QReview("r");
        QUser u = new QUser("u");

        /* 페이징 설정 */
        final int pageLength = 6;  // 한 페이지당 아이템 개수
        final int pageStart = (commonRequest.getPage() - 1) * pageLength;

        return jpaQueryFactory
                .select(Projections.fields(ReviewDTO.Item.class,
                        r.reviewIdx,
                        r.rating,
                        r.reviewText,
                        r.insertAt,
                        ExpressionUtils.as(u.userEmail, "maskingUserEmail")))
                .from(r)
                .innerJoin(u).on(r.userIdx.eq(u.userIdx))
                .where(
                        r.movieIdx.eq(movieIdx),
                        r.reviewIdx.loe(lastReviewIdx)
                )
                .orderBy(
                        r.insertAt.desc(),
                        r.reviewIdx.desc()
                )
                .offset(pageStart)
                .limit(6)
                .fetch();
    }

}
