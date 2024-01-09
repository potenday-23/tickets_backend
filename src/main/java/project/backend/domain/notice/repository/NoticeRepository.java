package project.backend.domain.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
