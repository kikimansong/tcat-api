package isol.tcat_api.domain.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NoticeDTO {

    /* Notice 단일 아이템 NoticeDTO.Item */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Item {
        private long noticeIdx;
        private int noticeTp;
        private String noticeTitle;
        private String noticeContents;
        private LocalDate startDate;
        private LocalDate endDate;
        private String bannerImg;
        private String isBanner;
        private LocalDateTime insertAt;
    }

    /* Notice 리스트 NoticeDTO.List */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class List<T> {
        private java.util.List<T> list; // java.util.List와 해당 클래스명 List가 겹쳐서 명시적으로 선언
        private Long totalCnt;
        private int page;
        private int pageStart;
        private int pageLength;
        private int lastPage;
    }

}
