package isol.tcat_api.domain.review.service;

import isol.tcat_api.domain.movie.repository.MovieRepository;
import isol.tcat_api.domain.review.dto.ReviewDTO;
import isol.tcat_api.domain.review.entity.Review;
import isol.tcat_api.domain.review.repository.ReviewCustomRepository;
import isol.tcat_api.domain.review.repository.ReviewRepository;
import isol.tcat_api.domain.user.entity.CustomUserDetails;
import isol.tcat_api.global.entity.CommonRequest;
import isol.tcat_api.global.error.ErrorCode;
import isol.tcat_api.global.error.GlobalException;
import isol.tcat_api.global.util.CommonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewCustomRepository reviewCustomRepository;
    private final MovieRepository movieRepository;
    private final CommonUtils commonUtils;

    @Transactional
    public ReviewDTO.List<ReviewDTO.Item> getList(long movieIdx, CommonRequest commonRequest, long lastReviewIdx) {
        movieRepository.findByMovieIdxAndIsDel(movieIdx, "N")
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        if (commonRequest.getPage() == 1) {
            lastReviewIdx = reviewCustomRepository.findLastIdxByMovieIdx(movieIdx);
        }

        long totalCnt = reviewRepository.countByMovieIdx(movieIdx);

        List<ReviewDTO.Item> list = reviewCustomRepository.findList(movieIdx, commonRequest, lastReviewIdx);

        /* Repository 에서 불러온 리스트 아이템들의 maskingUserEmail 컬럼을 마스킹 처리  */
        List<ReviewDTO.Item> maskedList = list.stream().map(item -> {
            String maskedEmail = commonUtils.maskEmail(item.getMaskingUserEmail());

            return ReviewDTO.Item.builder()
                    .reviewIdx(item.getReviewIdx())
                    .rating(item.getRating())
                    .reviewText(item.getReviewText())
                    .insertAt(item.getInsertAt())
                    .maskingUserEmail(maskedEmail)
                    .build();
        }).collect(Collectors.toList());

        return ReviewDTO.List.<ReviewDTO.Item>builder()
                .list(maskedList)
                .page(commonRequest.getPage())
                .totalCnt(totalCnt)
                .lastReviewIdx(lastReviewIdx)
                .build();
    }

    @Transactional
    public void insertReview(ReviewDTO.Create param) {
        if (param.getRating() == 0) {
            throw new GlobalException(ErrorCode.REVIEW_FAILED_NO_RATING);
        }

        if (param.getReviewText() == null || param.getReviewText().equals("")) {
            throw new GlobalException(ErrorCode.REVIEW_FAILED_NO_TEXT);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        LocalDateTime now = LocalDateTime.now();

        reviewRepository.save(Review.builder()
                        .movieIdx(param.getMovieIdx())
                        .userIdx(customUserDetails.getUser().getUserIdx())
                        .rating(param.getRating())
                        .reviewText(param.getReviewText())
                        .insertAt(now)
                .build());
    }

}
