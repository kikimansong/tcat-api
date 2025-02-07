package isol.tcat_api.domain.notice.service;

import isol.tcat_api.domain.notice.dto.NoticeDTO;
import isol.tcat_api.domain.notice.entity.Notice;
import isol.tcat_api.domain.notice.repository.NoticeCustomRepository;
import isol.tcat_api.domain.notice.repository.NoticeRepository;
import isol.tcat_api.global.entity.CommonRequest;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.GlobalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeCustomRepository noticeCustomRepository;

    /**
     * 공지사항 목록 검색
     * @param noticeTp 공지사항 타입 - 0: 공지사항 | 1: 이벤트
     * @param noticeTitle 공지사항 제목
     * @param commonRequest 검색 조건이 담긴 객체
     */
    @Transactional
    public NoticeDTO.List<NoticeDTO.Item> getList(String noticeTp, String noticeTitle, CommonRequest commonRequest) {
        return noticeCustomRepository.findList(noticeTp, noticeTitle, commonRequest);
    }

    /**
     * 공지사항 단일 검색
     * @param noticeIdx 공지사항 PK
     */
    @Transactional
    public NoticeDTO.Item getItem(long noticeIdx) {
        Notice noticeItem = noticeRepository.findByNoticeIdxAndIsDel(noticeIdx, "N").orElseThrow(()
            -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        return NoticeDTO.Item.builder()
                .noticeIdx(noticeItem.getNoticeIdx())
                .noticeTitle(noticeItem.getNoticeTitle())
                .noticeContents(noticeItem.getNoticeContents())
                .noticeTp(noticeItem.getNoticeTp())
                .insertAt(noticeItem.getInsertAt())
                .startDate(noticeItem.getStartDate())
                .endDate(noticeItem.getEndDate())
                .build();
    }

    /**
     * 메인화면 공지사항 리스트
     * @param noticeTp 공지사항 타입 - 0: 공지사항 | 1: 이벤트
     */
    @Transactional
    public NoticeDTO.List<NoticeDTO.Item> getMainList(int noticeTp) {
        switch (noticeTp) {
            case 0 -> {
                return noticeCustomRepository.findMainNoticeList();
            }
            case 1 -> {
                return noticeCustomRepository.findMainEventList();
            }
            default -> {
                return null;
            }
        }
    }

    /**
     * 메인화면 이벤트 배너 리스트
     */
    @Transactional
    public NoticeDTO.List<NoticeDTO.Item> getMainBannerList() {
        return noticeCustomRepository.findMainBannerList();
    }

}
