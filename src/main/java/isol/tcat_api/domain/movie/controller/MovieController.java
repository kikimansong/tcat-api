package isol.tcat_api.domain.movie.controller;

import isol.tcat_api.domain.movie.dto.MovieDTO;
import isol.tcat_api.domain.movie.service.MovieService;
import isol.tcat_api.global.entity.CommonRequest;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    /**
     * 영화 목록 검색
     * /api/v1/movie/list?page=1 [page] 쿼리스트링을 필수로 포함해서 호출
     * @param release 상영여부 - 0: 상영중 | 1: 상영 예정 | null : 전체
     * @param movieName 영화제목
     * @param commonRequest 검색 조건이 담긴 객체
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResponse<?>> getList(
            @RequestParam(name = "release", required = false) String release,
            @RequestParam(name = "movieName", required = false) String movieName,
            @ModelAttribute CommonRequest commonRequest) {
        MovieDTO.List<MovieDTO.Item> list = movieService.getList(release, movieName, commonRequest);
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    /**
     * 메인화면 영화 리스트 (예매율 Top 4)
     */
    @GetMapping("/list/main")
    public ResponseEntity<CommonResponse<?>> getMainList() {
        List<MovieDTO.Item> list = movieService.getMainList();
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    /**
     * 영화 단일 검색
     * @param movieIdx 영화 PK
     */
    @GetMapping("/{movieIdx}")
    public ResponseEntity<CommonResponse<?>> getItem(@PathVariable(value = "movieIdx") long movieIdx) {
        MovieDTO.Item item = movieService.getItem(movieIdx);
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(item)
                .build(), HttpStatus.OK);
    }

    /**
     * 예매 - 영화 목록 조회
     */
    @GetMapping("/reservation-list")
    public ResponseEntity<CommonResponse<?>> getListOnReservation() {
        MovieDTO.List<MovieDTO.Item> list = movieService.getListOnReservation();

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

}
