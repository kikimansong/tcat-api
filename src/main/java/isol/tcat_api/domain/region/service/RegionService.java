package isol.tcat_api.domain.region.service;

import isol.tcat_api.domain.cinema.dto.CinemaDTO;
import isol.tcat_api.domain.cinema.repository.CinemaCustomRepository;
import isol.tcat_api.domain.region.dto.RegionDTO;
import isol.tcat_api.domain.region.repository.RegionCustomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionCustomRepository regionCustomRepository;
    private final CinemaCustomRepository cinemaCustomRepository;

    /**
     * 예매 - 지역, 극장 목록 조회
     */
    @Transactional
    public List<RegionDTO.ItemWithCount> getListOnReservation() {
        List<RegionDTO.ItemWithCount> regionList = regionCustomRepository.findListOnReservation();

        /* regionList의 regionIdx를 배열로 WHERE IN 조건으로 파라미터 전달 */
        List<CinemaDTO.Item> cinemaList = cinemaCustomRepository.findByRegionIdxIn(
                regionList.stream().map(RegionDTO.ItemWithCount :: getRegionIdx).collect(Collectors.toList())
        );

        /* 조회한 cinemaList의 요소들을 regionIdx 기준으로 그룹핑하여 맵에 저장 */
        Map<Long, List<CinemaDTO.Item>> groupedCinemaMap = cinemaList.stream()
                .collect(Collectors.groupingBy(CinemaDTO.Item :: getRegionIdx));

        /* regionList에 cinemaList를 매핑 */
        for (RegionDTO.ItemWithCount region : regionList) {
            List<CinemaDTO.Item> cinemaListForRegion
                    = groupedCinemaMap.getOrDefault(region.getRegionIdx(), Collections.emptyList());

            region.setCinemaList(cinemaListForRegion);
        }

        return regionList;
    }

}
