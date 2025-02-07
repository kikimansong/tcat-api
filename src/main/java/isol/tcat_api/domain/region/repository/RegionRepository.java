package isol.tcat_api.domain.region.repository;

import isol.tcat_api.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {

}
