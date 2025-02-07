package isol.tcat_api.domain.reservation.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import isol.tcat_api.domain.movieroommapping.entity.MovieRoomMapping;
import isol.tcat_api.domain.movieroommapping.repository.MovieRoomMappingRepository;
import isol.tcat_api.domain.reservation.dto.ReservationDTO;
import isol.tcat_api.domain.reservation.entity.Reservation;
import isol.tcat_api.domain.reservation.respository.ReservationCustomRepository;
import isol.tcat_api.domain.reservation.respository.ReservationRepository;
import isol.tcat_api.domain.user.entity.CustomUserDetails;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.ExtendedGlobalException;
import isol.tcat_api.global.error.GlobalException;
import isol.tcat_api.global.util.CommonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationCustomRepository reservationCustomRepository;
    private final MovieRoomMappingRepository movieRoomMappingRepository;
    private final ObjectMapper objectMapper;
    private final CommonUtils commonUtils;

    /**
     * 극장 상영관 총 예약 좌석 수, 예약 좌석 조회
     * Reservation 테이블의 예매된 모든 좌석 수, 좌석 JSON 객체를 병합 함
     */
    @Transactional
    public ReservationDTO.ReservedSeatsItem getReservedSeats(long movieRoomMappingIdx) throws Exception {
        List<ReservationDTO.ReservedSeatsItem> list = reservationCustomRepository.findReservedSeats(movieRoomMappingIdx);

        int mergedCnt = 0;
        ObjectNode mergedNode = objectMapper.createObjectNode();

        /* 가져온 데이터 리스트의 좌석수, 예약 좌석 JSON 병합 */
        for (ReservationDTO.ReservedSeatsItem item : list) {
            mergedCnt += item.getReservationCnt();
            JsonNode currentNode = objectMapper.readTree(item.getReservationSeat());

            currentNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode value = entry.getValue();

                if (mergedNode.has(key)) {
                    // 키가 이미 존재하는 경우
                    if (value.isArray() && mergedNode.get(key).isArray()) {
                        // 병합
                        ArrayNode existingArray = (ArrayNode) mergedNode.get(key);
                        ArrayNode newArray = (ArrayNode) value;

                        newArray.forEach(e -> {
                            boolean exists = false;

                            for (JsonNode existingElement : existingArray) {
                                if (existingElement.equals(e)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                existingArray.add(e); // 중복되지 않으면 추가
                            }
                        });
                    }
                } else {
                    // 키가 존재하지 않는 경우
                    mergedNode.set(key, value);
                }
            });
        }

        ReservationDTO.ReservedSeatsItem result = new ReservationDTO.ReservedSeatsItem();

        result.setReservationCnt(mergedCnt);
        result.setReservationSeat(objectMapper.writeValueAsString(mergedNode));

        return result;
    }

    /**
     * 에매하기 Insert
     */
    @Transactional
    public void insertReservation(ReservationDTO.Create param) throws Exception {
        MovieRoomMapping movieRoomMappingItem =  movieRoomMappingRepository.findByMovieRoomMappingIdx(param.getMovieRoomMappingIdx()).orElseThrow(()
                -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        long compareTime = Duration.between(now, movieRoomMappingItem.getStartAt()).toMinutes();

        /* 영화 상영시간 까지 2시간이 남지 않으면 예매 불가능 */
        if (compareTime < 120) {
            throw new GlobalException(ErrorCode.RESERVATION_FAILED_BY_DATETIME);
        }

        ReservationDTO.ReservedSeatsItem reservedSeatsItem = getReservedSeats(param.getMovieRoomMappingIdx());

        /* JSON 문자열을 Map으로 변환 */
        Map<String, List<Integer>> map1 = commonUtils.jsonToMap(reservedSeatsItem.getReservationSeat());
        Map<String, List<Integer>> map2 = commonUtils.jsonToMap(param.getReservationSeat());

        /* 겹치는 값 찾기 */
        Map<String, Set<Integer>> commonValues = commonUtils.findSameKeyValue(map1, map2);

        /* 선택한 좌석이 이미 예매 된 상태면 예매 실패 */
        if (!commonValues.isEmpty()) {
            throw new ExtendedGlobalException(ErrorCode.RESERVATION_FAILED_ALREADY_RESERVED, commonValues);
        }

        /* SecurityContext에서 유저 정보 가져오기 */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        reservationRepository.save(Reservation.builder()
                        .movieRoomMappingIdx(param.getMovieRoomMappingIdx())
                        .reservationCnt(param.getReservationCnt())
                        .reservationSeat(param.getReservationSeat())
                        .userIdx(customUserDetails.getUser().getUserIdx())
                        .build());
    }

    public synchronized void test(ReservationDTO.Create param) throws Exception {
        insertReservation(param);
    }

}
