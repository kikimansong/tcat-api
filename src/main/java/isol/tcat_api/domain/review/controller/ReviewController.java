package isol.tcat_api.domain.review.controller;

import isol.tcat_api.domain.review.dto.ReviewDTO;
import isol.tcat_api.domain.review.service.ReviewService;
import isol.tcat_api.global.entity.CommonRequest;
import isol.tcat_api.global.entity.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 감상평 리스트 조회 (관람객 평점)
     * @param movieIdx 영화 PK
     * @param commonRequest 페이지 검색에 사용 될 Request 객체
     * @param lastReviewIdx 커서 기반 검색에 사용 될 마지막 감상평 PK
     */
    @GetMapping("/{movieIdx}")
    public ResponseEntity<CommonResponse<?>> getList(
            @PathVariable long movieIdx,
            @ModelAttribute CommonRequest commonRequest,
            @RequestParam long lastReviewIdx) {
        ReviewDTO.List<ReviewDTO.Item> list =  reviewService.getList(movieIdx, commonRequest, lastReviewIdx);
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .data(list)
                .build(), HttpStatus.OK);
    }

    /**
     * 감상평 (평점) 등록
     */
    @PostMapping("")
    public ResponseEntity<CommonResponse<?>> insertReview(@RequestBody ReviewDTO.Create param) {
        reviewService.insertReview(param);
        return new ResponseEntity<>(CommonResponse.builder()
                .code(200)
                .message("success")
                .build(), HttpStatus.OK);
    }

}
