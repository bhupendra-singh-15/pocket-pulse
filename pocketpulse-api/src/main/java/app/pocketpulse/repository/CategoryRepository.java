package app.pocketpulse.repository;

import app.pocketpulse.entity.CategoryEntity;
import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByProfileOrderByCreatedAtDesc(ProfileEntity profile);

    boolean existsByNameIgnoreCaseAndProfile(String name, ProfileEntity profile);

    Optional<CategoryEntity> findByIdAndProfile(Long id, ProfileEntity profile);

    List<CategoryEntity> findByTypeAndProfile(CategoryType type, ProfileEntity profile);
}