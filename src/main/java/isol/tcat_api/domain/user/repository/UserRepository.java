package isol.tcat_api.domain.user.repository;

import isol.tcat_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Long countByUserEmail(String userEmail);
    Optional<User> findByUserEmail(String userEmail);

}
