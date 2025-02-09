package isol.tcat_api.domain.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovieDTO {

    /* Movie 단일 아이템 MovieDTO.Item */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Item {
        private long movieIdx;
        private String movieName;
        private String movieImg;
        private int movieAge;
        private LocalDate movieOpenDt;
        private String movieDescription;
        private LocalTime movieTime;
        private long totalReservationCount;
        private double totalRating;
        private boolean isOpen;
    }

    /* Movie 리스트 MovieDTO.List */
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
