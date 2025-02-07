package isol.tcat_api.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReservationDTO {

    /* 예약된 좌석 조회 아이템 ReservationDTO.ReservedSeatsItem */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ReservedSeatsItem {
        private int reservationCnt;
        private String reservationSeat;
    }

    /* 예매 Insert ReservationDTO.Create */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Create {
        private long movieRoomMappingIdx;
        private int reservationCnt;
        private String reservationSeat;
    }

}
