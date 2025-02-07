package isol.tcat_api.domain.movieroommapping.repository;

import isol.tcat_api.domain.movieroommapping.entity.MovieRoomMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRoomMappingRepository extends JpaRepository<MovieRoomMapping, Long> {

    Optional<MovieRoomMapping> findByMovieRoomMappingIdx(long movieRoomMappingIdx);

}
