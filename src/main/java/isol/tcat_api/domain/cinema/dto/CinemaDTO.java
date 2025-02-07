package isol.tcat_api.domain.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CinemaDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Item {
        private long regionIdx;
        private long cinemaIdx;
        private String cinemaName;
    }

}
