package isol.tcat_api.domain.moviecinemamapping.service;

import isol.tcat_api.domain.moviecinemamapping.dto.MovieCinemaMappingDTO;
import isol.tcat_api.domain.moviecinemamapping.repository.MovieCinemaMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCinemaMappingService {

    private final MovieCinemaMappingRepository movieCinemaMappingRepository;

    public List<MovieCinemaMappingDTO.ReservationItem> getListOnReservation(long movieIdx, long cinemaIdx, LocalDate startAt) {
        List<Object[]> queryList = movieCinemaMappingRepository.findListOnReservation(movieIdx, cinemaIdx, startAt);

        List<MovieCinemaMappingDTO.ReservationItem> resultList = queryList.stream()
                .map(row -> MovieCinemaMappingDTO.ReservationItem.builder()
                        .movieRoomMappingIdx((Long) row[0])
                        .roomIdx((Long) row[1])
                        .roomName((String) row[2])
                        .roomSeatCnt((Integer) row[3])
                        .movieName((String) row[4])
                        .startAt(((Timestamp) row[5]).toLocalDateTime())
                        .endAt(((Timestamp) row[6]).toLocalDateTime())
                        .possibleSeatCnt((Long) row[7])
                        .isAvailable((Boolean) row[8])
                        .build())
                .collect(Collectors.toList());

        return resultList;
    }

}
