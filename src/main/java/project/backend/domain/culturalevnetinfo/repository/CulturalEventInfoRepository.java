package project.backend.domain.culturalevnetinfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.culturalevnetinfo.entity.CulturalEventInfo;

public interface CulturalEventInfoRepository extends JpaRepository<CulturalEventInfo, Long> {
}
