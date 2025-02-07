package isol.tcat_api.domain.room.controller;

import isol.tcat_api.domain.room.dto.RoomDTO;
import isol.tcat_api.domain.room.service.RoomService;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;

    /**
     * 예매 - 극장 상영관 조회
     * @param movieRoomMappingIdx 영화-상영관 매핑 PK
     * @param roomIdx 극장 상영관 PK
     */
    @GetMapping("/reservation-item/{movieRoomMappingIdx}/{roomIdx}")
    public ResponseEntity<CommonResponse<?>> getItemOnReservation(
            @PathVariable(value = "movieRoomMappingIdx") long movieRoomMappingIdx,
            @PathVariable(value = "roomIdx") long roomIdx) throws Exception {
        RoomDTO.Item item = roomService.getItemOnReservation(movieRoomMappingIdx, roomIdx);

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(item)
                .build(), HttpStatus.OK);
    }

}
