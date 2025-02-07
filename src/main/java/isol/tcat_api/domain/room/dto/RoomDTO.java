package isol.tcat_api.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RoomDTO {

    /* Room 단일 아이템 RoomDTO.Item */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Item {
        private long roomIdx;
        private String roomName;
        private int roomSeatCnt;
        private String roomBlankSeat;
        private int reservationCnt;
        private String reservationSeat;
        private int possibleSeatCnt;
    }

}
