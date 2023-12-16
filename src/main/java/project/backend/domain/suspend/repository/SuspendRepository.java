package project.backend.domain.suspend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.suspend.entity.Suspend;
import java.util.Optional;

public interface SuspendRepository extends JpaRepository<Suspend, Long> {
    Optional<Suspend> findFirstByMemberId(Long memberId);
}
