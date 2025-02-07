package isol.tcat_api.domain.reservation.controller;

import isol.tcat_api.domain.reservation.dto.ReservationDTO;
import isol.tcat_api.domain.reservation.service.ReservationService;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 극장 상영관 예약좌석 정보 조회 (현재 사용 안함)
     */
    @GetMapping("/reserved-seats/{movieRoomMappingIdx}")
    public ResponseEntity<CommonResponse<?>> getReservedSeats(@PathVariable(value = "movieRoomMappingIdx") long movieRoomMappingIdx) throws Exception {
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(reservationService.getReservedSeats(movieRoomMappingIdx))
                .build(), HttpStatus.OK);
    }

    /**
     * 예매하기
     */
    @PostMapping("")
    public ResponseEntity<CommonResponse<?>> insertReservation(@RequestBody ReservationDTO.Create param) throws Exception {
        reservationService.insertReservation(param);
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("예매가 완료되었습니다.")
                .data(null)
                .build(), HttpStatus.OK);
    }
}