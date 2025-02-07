package isol.tcat_api.domain.region.dto;

import isol.tcat_api.domain.cinema.dto.CinemaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class RegionDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ItemWithCount {
        private long regionIdx;
        private String regionName;
        private long cinemaCount;
        private List<CinemaDTO.Item> cinemaList;
    }

}
