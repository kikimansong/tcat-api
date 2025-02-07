package isol.tcat_api.domain.room.repository;

import isol.tcat_api.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomIdxAndIsDel(long roomIdx, String isDel);

}
