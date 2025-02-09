package isol.tcat_api.domain.movie.service;

import isol.tcat_api.domain.movie.dto.MovieDTO;
import isol.tcat_api.domain.movie.entity.Movie;
import isol.tcat_api.domain.movie.repository.MovieCustomRepository;
import isol.tcat_api.domain.movie.repository.MovieRepository;
import isol.tcat_api.global.entity.CommonRequest;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.GlobalException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieCustomRepository movieCustomRepository;

    /**
     * 영화 목록 검색
     * @param release 상영여부 - 0: 상영중 | 1: 상영 예정 | null : 전체
     * @param movieName 영화제목
     * @param commonRequest 검색 조건이 담긴 객체
     */
    @Transactional
    public MovieDTO.List<MovieDTO.Item> getList(String release, String movieName, CommonRequest commonRequest) {
        return movieCustomRepository.findList(release, movieName, commonRequest);
    }

    /**
     * 메인화면 영화 목록 조회 (예매율 Top 4)
     */
    @Transactional
    public List<MovieDTO.Item> getMainList() {
        return movieCustomRepository.findMainList();
    }

    /**
     * 영화 단일 검색
     * @param movieIdx 영화 PK
     */
    @Transactional
    public MovieDTO.Item getItem(long movieIdx) {
        Movie movieItem  = movieRepository.findByMovieIdxAndIsDel(movieIdx, "N").orElseThrow(()
                -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        // 누적 관객 수
        Long totalReservationCount = movieCustomRepository.findTotalReservationCount(movieIdx);
        // 평점
        Double totalRating = movieCustomRepository.findTotalRating(movieIdx) ;

        return MovieDTO.Item.builder()
                .movieIdx(movieItem.getMovieIdx())
                .movieName(movieItem.getMovieName())
                .movieAge(movieItem.getMovieAge())
                .movieImg(movieItem.getMovieImg())
                .movieDescription(movieItem.getMovieDescription())
                .movieOpenDt(movieItem.getMovieOpenDt())
                .isOpen(movieItem.getMovieOpenDt().isBefore(LocalDate.now()))
                .movieTime(movieItem.getMovieTime())
                .totalReservationCount(totalReservationCount)
                .totalRating(totalRating == null ? 0 : totalRating)
                .build();
    }

    /**
     * 예메 - 영화 목록 조회
     */
    @Transactional
    public MovieDTO.List<MovieDTO.Item> getListOnReservation() {
        return movieCustomRepository.findListOnReservation();
    }

}
