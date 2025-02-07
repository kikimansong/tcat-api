package isol.tcat_api.domain.notice.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isol.tcat_api.domain.notice.dto.NoticeDTO;
import isol.tcat_api.domain.notice.entity.QNotice;
import isol.tcat_api.global.entity.CommonRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public NoticeDTO.List<NoticeDTO.Item> findList(String noticeTp, String noticeTitle, CommonRequest commonRequest) {
        QNotice n = new QNotice("n");

        BooleanExpression noticeTpWhere = null;
        BooleanExpression noticeTitleWhere = null;

        /* 공지사항 타입 검색 */
        if (noticeTp != null && !noticeTp.equals("")) {
            noticeTpWhere = n.noticeTp.eq(Integer.valueOf(noticeTp));
        }

        /* 공지사항 제목 검색 */
        if (noticeTitle != null && !noticeTitle.equals("")) {
            noticeTitleWhere = n.noticeTitle.contains(noticeTitle);
        }

        /* 페이징 설정 */
        final int pageLength = 10;
        final int pageStart = (commonRequest.getPage() - 1) * pageLength;

        List<NoticeDTO.Item> queryList = jpaQueryFactory.select(Projections.fields(NoticeDTO.Item.class,
                n.noticeIdx,
                n.noticeTitle,
                n.noticeContents,
                n.noticeTp,
                n.insertAt))
                .from(n)
                .where(
                        n.isDel.eq("N"),
                        noticeTpWhere,
                        noticeTitleWhere
                )
                .orderBy(
                        n.insertAt.desc(),
                        n.noticeIdx.desc()
                )
                .offset(pageStart)
                .limit(pageLength)
                .fetch();

        Long queryTotalCnt = jpaQueryFactory.select(n.noticeIdx.count())
                .from(n)
                .where(
                        n.isDel.eq("N"),
                        noticeTpWhere,
                        noticeTitleWhere
                )
                .fetchOne();

        return NoticeDTO.List.<NoticeDTO.Item>builder()
                .list(queryList)
                .totalCnt(queryTotalCnt)
                .page(commonRequest.getPage())
                .lastPage((int) Math.ceil((double) queryTotalCnt.intValue() / pageLength))
                .pageStart(pageStart)
                .pageLength(pageLength)
                .build();
    }

    /**
     * 메인화면 공지사항 리스트
     */
    public NoticeDTO.List<NoticeDTO.Item> findMainNoticeList() {
        QNotice n = new QNotice("n");

        List<NoticeDTO.Item> queryList = jpaQueryFactory.select(Projections.fields(NoticeDTO.Item.class,
                        n.noticeIdx,
                        n.noticeTitle,
                        n.noticeContents,
                        n.noticeTp,
                        n.insertAt))
                .from(n)
                .where(
                        n.isDel.eq("N"),
                        n.noticeTp.eq(0)
                )
                .orderBy(
                        n.insertAt.desc(),
                        n.noticeIdx.desc()
                )
                .limit(6)
                .fetch();

        return NoticeDTO.List.<NoticeDTO.Item>builder()
                .list(queryList)
                .build();
    }

    /**
     * 메인화면 이벤트 리스트
     */
    public NoticeDTO.List<NoticeDTO.Item> findMainEventList() {
        QNotice n = new QNotice("n");

        List<NoticeDTO.Item> queryList = jpaQueryFactory.select(Projections.fields(NoticeDTO.Item.class,
                        n.noticeIdx,
                        n.noticeTitle,
                        n.noticeContents,
                        n.noticeTp,
                        n.insertAt))
                .from(n)
                .where(
                        n.isDel.eq("N"),
                        n.noticeTp.eq(1),
                        Expressions.dateTemplate(LocalDate.class, "CAST(now() AS date)").between(n.startDate, n.endDate)
                )
                .orderBy(
                        n.startDate.desc(),
                        n.insertAt.desc(),
                        n.noticeIdx.desc()
                )
                .limit(6)
                .fetch();

        return NoticeDTO.List.<NoticeDTO.Item>builder()
                .list(queryList)
                .build();
    }

    /**
     * 메인화면 이벤트 배너 리스트
     */
    public NoticeDTO.List<NoticeDTO.Item> findMainBannerList() {
        QNotice n = new QNotice("n");

        List<NoticeDTO.Item> queryList = jpaQueryFactory.select(Projections.fields(NoticeDTO.Item.class,
                n.noticeIdx,
                n.noticeTitle,
                n.bannerImg))
                .from(n)
                .where(
                        n.isDel.eq("N"),
                        n.noticeTp.eq(1),
                        n.isBanner.eq("Y"),
                        Expressions.dateTemplate(LocalDate.class, "CAST(now() AS date)").between(n.startDate, n.endDate)
                )
                .orderBy(
                        n.startDate.desc(),
                        n.insertAt.desc(),
                        n.noticeIdx.desc()
                )
                .fetch();

        return NoticeDTO.List.<NoticeDTO.Item>builder()
                .list(queryList)
                .build();
    }

}
