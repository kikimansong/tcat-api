package isol.tcat_api.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReviewDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Create {
        private long movieIdx;
        private long userIdx;
        private int rating;
        private String reviewText;
        private LocalDateTime insertAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Item {
        private long reviewIdx;
        private int rating;
        private String reviewText;
        private LocalDateTime insertAt;
        private String maskingUserEmail;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class List<T> {
        private java.util.List<T> list; // java.util.List와 해당 클래스명 List가 겹쳐서 명시적으로 선언
        private int page;
        private long totalCnt;
        private long lastReviewIdx;
    }

}
