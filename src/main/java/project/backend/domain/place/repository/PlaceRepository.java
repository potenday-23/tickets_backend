package project.backend.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.place.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
}
