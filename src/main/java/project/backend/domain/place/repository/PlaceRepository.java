package project.backend.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.backend.domain.member.entity.Member;
import project.backend.domain.member.entity.SocialType;
import project.backend.domain.place.entity.Place;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findFirstByAddress(String address);
}
