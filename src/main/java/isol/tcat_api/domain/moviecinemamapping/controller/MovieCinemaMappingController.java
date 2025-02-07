package isol.tcat_api.domain.moviecinemamapping.controller;

import isol.tcat_api.domain.moviecinemamapping.dto.MovieCinemaMappingDTO;
import isol.tcat_api.domain.moviecinemamapping.service.MovieCinemaMappingService;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie-cinema-mapping")
public class MovieCinemaMappingController {

    private final MovieCinemaMappingService movieCinemaMappingService;

    /**
     * 예매 - 날짜/시간 탭의 데이터 조회
     * @param movieIdx 영화 PK
     * @param cinemaIdx 극장 PK
     * @param startAt 날짜
     */
    @GetMapping("/reservation-list")
    public ResponseEntity<CommonResponse<?>> getListOnReservation(
            @RequestParam(name = "movieIdx") long movieIdx,
            @RequestParam(name = "cinemaIdx") long cinemaIdx,
            @RequestParam(name = "startAt") LocalDate startAt) {
        List<MovieCinemaMappingDTO.ReservationItem> list = movieCinemaMappingService.getListOnReservation(movieIdx, cinemaIdx, startAt);

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

}
