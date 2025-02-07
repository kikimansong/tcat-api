package isol.tcat_api.domain.movie.repository;

import isol.tcat_api.domain.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByMovieIdxAndIsDel(long movieIdx, String isDel);

}
