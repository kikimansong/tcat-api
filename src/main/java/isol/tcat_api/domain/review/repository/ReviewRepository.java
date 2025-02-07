package isol.tcat_api.domain.review.repository;

import isol.tcat_api.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Long countByMovieIdx(long movieIdx);

}
