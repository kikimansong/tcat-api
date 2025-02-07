package isol.tcat_api.domain.moviecinemamapping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MovieCinemaMappingDTO {

    /* 예매 - 날짜/시간 단일 아이템 MovieCinemaMappingDTO.ReservationItem */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ReservationItem {
        private long movieRoomMappingIdx;
        private long roomIdx;
        private String movieName;
        private String roomName;
        private int roomSeatCnt;
        private long possibleSeatCnt;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private boolean isAvailable;
    }

}
