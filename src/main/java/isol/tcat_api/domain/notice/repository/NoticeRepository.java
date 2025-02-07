package isol.tcat_api.domain.notice.repository;

import isol.tcat_api.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findByNoticeIdxAndIsDel(long noticeIdx, String isDel);

}
