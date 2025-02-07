package isol.tcat_api.domain.room.service;

import isol.tcat_api.domain.reservation.dto.ReservationDTO;
import isol.tcat_api.domain.reservation.service.ReservationService;
import isol.tcat_api.domain.room.dto.RoomDTO;
import isol.tcat_api.domain.room.entity.Room;
import isol.tcat_api.domain.room.repository.RoomRepository;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.GlobalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final ReservationService reservationService;

    /**
     * 예매 - 극장 상영관 조회
     * 극장 상영관 데이터와 해당 상영관의 예약된 정보(좌석 수, 예약 좌석)를 같이 조회 함.
     */
    @Transactional
    public RoomDTO.Item getItemOnReservation(long movieRoomMappingIdx, long roomIdx) throws Exception {
        Room roomItem = roomRepository.findByRoomIdxAndIsDel(roomIdx, "N").orElseThrow(()
            -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        /* 예약 정보 */
        ReservationDTO.ReservedSeatsItem reservationItem = reservationService.getReservedSeats(movieRoomMappingIdx);

        return RoomDTO.Item.builder()
                .roomIdx(roomItem.getRoomIdx())
                .roomName(roomItem.getRoomName())
                .roomSeatCnt(roomItem.getRoomSeatCnt())
                .roomBlankSeat(roomItem.getRoomBlankSeat())
                .reservationCnt(reservationItem.getReservationCnt())
                .reservationSeat(reservationItem.getReservationSeat())
                .possibleSeatCnt(roomItem.getRoomSeatCnt() - reservationItem.getReservationCnt())
                .build();
    }

}
