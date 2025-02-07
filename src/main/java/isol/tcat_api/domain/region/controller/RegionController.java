package isol.tcat_api.domain.region.controller;

import isol.tcat_api.domain.region.dto.RegionDTO;
import isol.tcat_api.domain.region.service.RegionService;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/region")
public class RegionController {

    private final RegionService regionService;

    /**
     * 예매 - 지역 목록 조회
     */
    @GetMapping("/reservation-list")
    public ResponseEntity<CommonResponse<?>> getListOnReservation() {
        List<RegionDTO.ItemWithCount> list = regionService.getListOnReservation();

        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

}
